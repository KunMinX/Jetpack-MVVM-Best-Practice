/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.puremusic.ui.page;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kunminx.architecture.ui.adapter.SimpleBaseBindingAdapter;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.bridge.request.MusicRequestViewModel;
import com.kunminx.puremusic.bridge.state.MainViewModel;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.databinding.AdapterPlayItemBinding;
import com.kunminx.puremusic.databinding.FragmentMainBinding;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.base.BaseFragment;
import com.kunminx.puremusic.ui.helper.DrawerCoordinateHelper;

/**
 * Create by KunMinX at 19/10/29
 */
public class MainFragment extends BaseFragment {

    private FragmentMainBinding mBinding;
    private MainViewModel mMainViewModel;
    private MusicRequestViewModel mMusicRequestViewModel;
    private SimpleBaseBindingAdapter<TestAlbum.TestMusic, AdapterPlayItemBinding> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainViewModel = getFragmentViewModelProvider(this).get(MainViewModel.class);
        mMusicRequestViewModel = getFragmentViewModelProvider(this).get(MusicRequestViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mBinding = FragmentMainBinding.bind(view);
        mBinding.setClick(new ClickProxy());
        mBinding.setVm(mMainViewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMainViewModel.initTabAndPage.set(true);

        mMainViewModel.pageAssetPath.set("summary.html");

        mAdapter = new SimpleBaseBindingAdapter<TestAlbum.TestMusic, AdapterPlayItemBinding>(getContext(), R.layout.adapter_play_item) {
            @Override
            protected void onSimpleBindItem(AdapterPlayItemBinding binding, TestAlbum.TestMusic item, RecyclerView.ViewHolder holder) {
                binding.tvTitle.setText(item.getTitle());
                binding.tvArtist.setText(item.getArtist().getName());
                Glide.with(binding.ivCover.getContext()).load(item.getCoverImg()).into(binding.ivCover);
                int currentIndex = PlayerManager.getInstance().getAlbumIndex();
                binding.ivPlayStatus.setColor(currentIndex == holder.getAdapterPosition()
                        ? getResources().getColor(R.color.gray) : Color.TRANSPARENT);
                binding.getRoot().setOnClickListener(v -> {
                    PlayerManager.getInstance().playAudio(holder.getAdapterPosition());
                });
            }
        };

        mBinding.rv.setAdapter(mAdapter);

        PlayerManager.getInstance().getChangeMusicLiveData().observe(getViewLifecycleOwner(), changeMusic -> {

            // TODO tip 1：所有播放状态的改变，都要通过这个 作为 唯一可信源 的 PlayerManager 来统一分发，

            // 如此才能方便 追溯事件源，以及 避免 不可预期的 推送和错误。
            // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249

            mAdapter.notifyDataSetChanged();
        });

        mMusicRequestViewModel.getFreeMusicsLiveData().observe(getViewLifecycleOwner(), musicAlbum -> {
            if (musicAlbum != null && musicAlbum.getMusics() != null) {
                //noinspection unchecked
                mAdapter.setList(musicAlbum.getMusics());
                mAdapter.notifyDataSetChanged();

                // TODO tip 4：未作 UnPeek 处理的 用于 request 的 LiveData，在视图控制器重建时会自动倒灌数据

                // 一定要记住这一点，因为如果没有妥善处理，这里就会出现预期外的错误，一定要记得它在重建时 是一定会倒灌的。

                // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0129483567

                if (PlayerManager.getInstance().getAlbum() == null ||
                        !PlayerManager.getInstance().getAlbum().getAlbumId().equals(musicAlbum.getAlbumId())) {
                    PlayerManager.getInstance().loadAlbum(musicAlbum);
                }
            }
        });

        if (PlayerManager.getInstance().getAlbum() == null) {
            mMusicRequestViewModel.requestFreeMusics();
        } else {
            mAdapter.setList(PlayerManager.getInstance().getAlbum().getMusics());
            mAdapter.notifyDataSetChanged();
        }

        DrawerCoordinateHelper.getInstance().openDrawer.observe(getViewLifecycleOwner(), aBoolean -> {
            mSharedViewModel.openOrCloseDrawer.setValue(true);
        });

    }


    // TODO tip 2：此处通过 DataBinding 来规避 在 setOnClickListener 时存在的 视图调用的一致性问题，

    // 也即，有绑定就有绑定，没绑定也没什么大不了的，总之 不会因一致性问题造成 视图调用的空指针。
    // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

    public class ClickProxy {

        public void openMenu() {

            // TODO tip 3：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

            // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一个思想是 最少知道原则，
            // Activity 内部的事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部的东西。
            // 因为 Activity 端的处理后续可能会改变，并且可受用于更多的 fragment，而不单单是本 fragment。

            mSharedViewModel.openOrCloseDrawer.setValue(true);
        }

        public void search() {
            nav().navigate(R.id.action_mainFragment_to_searchFragment);
        }

    }

}
