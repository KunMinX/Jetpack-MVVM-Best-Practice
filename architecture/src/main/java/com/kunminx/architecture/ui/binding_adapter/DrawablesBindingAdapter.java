/*
 * Copyright 2018-present KunMinX
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

package com.kunminx.architecture.ui.binding_adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.databinding.BindingAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by linjiang on 2018/9/5.
 */

public class DrawablesBindingAdapter {
    private static final String TAG = "Drawables";

    private static final int INVALID = 0;
    private static final int[] TMP_PADDING = new int[4];


    // normal, checked, checkable, enabled, focused, pressed, selected
    @BindingAdapter(value = {
            "drawable_shapeMode",
            "drawable_solidColor",
            "drawable_strokeColor",
            "drawable_strokeWidth",
            "drawable_strokeDash",
            "drawable_strokeDashGap",
            "drawable_radius",
            "drawable_radiusLT",
            "drawable_radiusLB",
            "drawable_radiusRT",
            "drawable_radiusRB",
            "drawable_startColor",
            "drawable_centerColor",
            "drawable_endColor",
            "drawable_orientation",
            "drawable_gradientType",
            "drawable_radialCenterX",
            "drawable_radialCenterY",
            "drawable_radialRadius",
            "drawable_width",
            "drawable_height",
            "drawable_marginLeft",
            "drawable_marginTop",
            "drawable_marginRight",
            "drawable_marginBottom",
            "drawable_ringThickness",
            "drawable_ringThicknessRatio",
            "drawable_ringInnerRadius",
            "drawable_ringInnerRadiusRatio",

            "drawable_checked_shapeMode",
            "drawable_checked_solidColor",
            "drawable_checked_strokeColor",
            "drawable_checked_strokeWidth",
            "drawable_checked_strokeDash",
            "drawable_checked_strokeDashGap",
            "drawable_checked_radius",
            "drawable_checked_radiusLT",
            "drawable_checked_radiusLB",
            "drawable_checked_radiusRT",
            "drawable_checked_radiusRB",
            "drawable_checked_startColor",
            "drawable_checked_centerColor",
            "drawable_checked_endColor",
            "drawable_checked_orientation",
            "drawable_checked_gradientType",
            "drawable_checked_radialCenterX",
            "drawable_checked_radialCenterY",
            "drawable_checked_radialRadius",
            "drawable_checked_width",
            "drawable_checked_height",
            "drawable_checked_marginLeft",
            "drawable_checked_marginTop",
            "drawable_checked_marginRight",
            "drawable_checked_marginBottom",
            "drawable_checked_ringThickness",
            "drawable_checked_ringThicknessRatio",
            "drawable_checked_ringInnerRadius",
            "drawable_checked_ringInnerRadiusRatio",

            "drawable_checkable_shapeMode",
            "drawable_checkable_solidColor",
            "drawable_checkable_strokeColor",
            "drawable_checkable_strokeWidth",
            "drawable_checkable_strokeDash",
            "drawable_checkable_strokeDashGap",
            "drawable_checkable_radius",
            "drawable_checkable_radiusLT",
            "drawable_checkable_radiusLB",
            "drawable_checkable_radiusRT",
            "drawable_checkable_radiusRB",
            "drawable_checkable_startColor",
            "drawable_checkable_centerColor",
            "drawable_checkable_endColor",
            "drawable_checkable_orientation",
            "drawable_checkable_gradientType",
            "drawable_checkable_radialCenterX",
            "drawable_checkable_radialCenterY",
            "drawable_checkable_radialRadius",
            "drawable_checkable_width",
            "drawable_checkable_height",
            "drawable_checkable_marginLeft",
            "drawable_checkable_marginTop",
            "drawable_checkable_marginRight",
            "drawable_checkable_marginBottom",
            "drawable_checkable_ringThickness",
            "drawable_checkable_ringThicknessRatio",
            "drawable_checkable_ringInnerRadius",
            "drawable_checkable_ringInnerRadiusRatio",

            "drawable_enabled_shapeMode",
            "drawable_enabled_solidColor",
            "drawable_enabled_strokeColor",
            "drawable_enabled_strokeWidth",
            "drawable_enabled_strokeDash",
            "drawable_enabled_strokeDashGap",
            "drawable_enabled_radius",
            "drawable_enabled_radiusLT",
            "drawable_enabled_radiusLB",
            "drawable_enabled_radiusRT",
            "drawable_enabled_radiusRB",
            "drawable_enabled_startColor",
            "drawable_enabled_centerColor",
            "drawable_enabled_endColor",
            "drawable_enabled_orientation",
            "drawable_enabled_gradientType",
            "drawable_enabled_radialCenterX",
            "drawable_enabled_radialCenterY",
            "drawable_enabled_radialRadius",
            "drawable_enabled_width",
            "drawable_enabled_height",
            "drawable_enabled_marginLeft",
            "drawable_enabled_marginTop",
            "drawable_enabled_marginRight",
            "drawable_enabled_marginBottom",
            "drawable_enabled_ringThickness",
            "drawable_enabled_ringThicknessRatio",
            "drawable_enabled_ringInnerRadius",
            "drawable_enabled_ringInnerRadiusRatio",

            "drawable_focused_shapeMode",
            "drawable_focused_solidColor",
            "drawable_focused_strokeColor",
            "drawable_focused_strokeWidth",
            "drawable_focused_strokeDash",
            "drawable_focused_strokeDashGap",
            "drawable_focused_radius",
            "drawable_focused_radiusLT",
            "drawable_focused_radiusLB",
            "drawable_focused_radiusRT",
            "drawable_focused_radiusRB",
            "drawable_focused_startColor",
            "drawable_focused_centerColor",
            "drawable_focused_endColor",
            "drawable_focused_orientation",
            "drawable_focused_gradientType",
            "drawable_focused_radialCenterX",
            "drawable_focused_radialCenterY",
            "drawable_focused_radialRadius",
            "drawable_focused_width",
            "drawable_focused_height",
            "drawable_focused_marginLeft",
            "drawable_focused_marginTop",
            "drawable_focused_marginRight",
            "drawable_focused_marginBottom",
            "drawable_focused_ringThickness",
            "drawable_focused_ringThicknessRatio",
            "drawable_focused_ringInnerRadius",
            "drawable_focused_ringInnerRadiusRatio",

            "drawable_pressed_shapeMode",
            "drawable_pressed_solidColor",
            "drawable_pressed_strokeColor",
            "drawable_pressed_strokeWidth",
            "drawable_pressed_strokeDash",
            "drawable_pressed_strokeDashGap",
            "drawable_pressed_radius",
            "drawable_pressed_radiusLT",
            "drawable_pressed_radiusLB",
            "drawable_pressed_radiusRT",
            "drawable_pressed_radiusRB",
            "drawable_pressed_startColor",
            "drawable_pressed_centerColor",
            "drawable_pressed_endColor",
            "drawable_pressed_orientation",
            "drawable_pressed_gradientType",
            "drawable_pressed_radialCenterX",
            "drawable_pressed_radialCenterY",
            "drawable_pressed_radialRadius",
            "drawable_pressed_width",
            "drawable_pressed_height",
            "drawable_pressed_marginLeft",
            "drawable_pressed_marginTop",
            "drawable_pressed_marginRight",
            "drawable_pressed_marginBottom",
            "drawable_pressed_ringThickness",
            "drawable_pressed_ringThicknessRatio",
            "drawable_pressed_ringInnerRadius",
            "drawable_pressed_ringInnerRadiusRatio",

            "drawable_selected_shapeMode",
            "drawable_selected_solidColor",
            "drawable_selected_strokeColor",
            "drawable_selected_strokeWidth",
            "drawable_selected_strokeDash",
            "drawable_selected_strokeDashGap",
            "drawable_selected_radius",
            "drawable_selected_radiusLT",
            "drawable_selected_radiusLB",
            "drawable_selected_radiusRT",
            "drawable_selected_radiusRB",
            "drawable_selected_startColor",
            "drawable_selected_centerColor",
            "drawable_selected_endColor",
            "drawable_selected_orientation",
            "drawable_selected_gradientType",
            "drawable_selected_radialCenterX",
            "drawable_selected_radialCenterY",
            "drawable_selected_radialRadius",
            "drawable_selected_width",
            "drawable_selected_height",
            "drawable_selected_marginLeft",
            "drawable_selected_marginTop",
            "drawable_selected_marginRight",
            "drawable_selected_marginBottom",
            "drawable_selected_ringThickness",
            "drawable_selected_ringThicknessRatio",
            "drawable_selected_ringInnerRadius",
            "drawable_selected_ringInnerRadiusRatio",

            // normal, checked, checkable, enabled, focused, pressed, selected

            "drawable",
            "drawable_checked",
            "drawable_checkable",
            "drawable_enabled",
            "drawable_focused",
            "drawable_pressed",
            "drawable_selected",

    }, requireAll = false)
    public static void setViewBackground(
            View view,

            @ShapeMode int shapeMode,
            @ColorInt Integer solidColor,
            @ColorInt int strokeColor,
            float strokeWidth,
            float strokeDash,
            float strokeDashGap,
            float radius,
            float radiusLT,
            float radiusLB,
            float radiusRT,
            float radiusRB,
            @ColorInt Integer startColor,
            @ColorInt Integer centerColor,
            @ColorInt Integer endColor,
            @Orientation int orientation,
            @GradientType int gradientType,
            Float radialCenterX,
            Float radialCenterY,
            float radialRadius,
            float width,
            float height,
            float marginLeft,
            float marginTop,
            float marginRight,
            float marginBottom,
            float ringThickness,
            float ringThicknessRatio,
            float ringInnerRadius,
            float ringInnerRadiusRatio,

            @ShapeMode int checked_shapeMode,
            @ColorInt Integer checked_solidColor,
            @ColorInt int checked_strokeColor,
            float checked_strokeWidth,
            float checked_strokeDash,
            float checked_strokeDashGap,
            float checked_radius,
            float checked_radiusLT,
            float checked_radiusLB,
            float checked_radiusRT,
            float checked_radiusRB,
            @ColorInt Integer checked_startColor,
            @ColorInt Integer checked_centerColor,
            @ColorInt Integer checked_endColor,
            @Orientation int checked_orientation,
            @GradientType int checked_gradientType,
            Float checked_radialCenterX,
            Float checked_radialCenterY,
            float checked_radialRadius,
            float checked_width,
            float checked_height,
            float checked_marginLeft,
            float checked_marginTop,
            float checked_marginRight,
            float checked_marginBottom,
            float checked_ringThickness,
            float checked_ringThicknessRatio,
            float checked_ringInnerRadius,
            float checked_ringInnerRadiusRatio,

            @ShapeMode int checkable_shapeMode,
            @ColorInt Integer checkable_solidColor,
            @ColorInt int checkable_strokeColor,
            float checkable_strokeWidth,
            float checkable_strokeDash,
            float checkable_strokeDashGap,
            float checkable_radius,
            float checkable_radiusLT,
            float checkable_radiusLB,
            float checkable_radiusRT,
            float checkable_radiusRB,
            @ColorInt Integer checkable_startColor,
            @ColorInt Integer checkable_centerColor,
            @ColorInt Integer checkable_endColor,
            @Orientation int checkable_orientation,
            @GradientType int checkable_gradientType,
            Float checkable_radialCenterX,
            Float checkable_radialCenterY,
            float checkable_radialRadius,
            float checkable_width,
            float checkable_height,
            float checkable_marginLeft,
            float checkable_marginTop,
            float checkable_marginRight,
            float checkable_marginBottom,
            float checkable_ringThickness,
            float checkable_ringThicknessRatio,
            float checkable_ringInnerRadius,
            float checkable_ringInnerRadiusRatio,

            @ShapeMode int enabled_shapeMode,
            @ColorInt Integer enabled_solidColor,
            @ColorInt int enabled_strokeColor,
            float enabled_strokeWidth,
            float enabled_strokeDash,
            float enabled_strokeDashGap,
            float enabled_radius,
            float enabled_radiusLT,
            float enabled_radiusLB,
            float enabled_radiusRT,
            float enabled_radiusRB,
            @ColorInt Integer enabled_startColor,
            @ColorInt Integer enabled_centerColor,
            @ColorInt Integer enabled_endColor,
            @Orientation int enabled_orientation,
            @GradientType int enabled_gradientType,
            Float enabled_radialCenterX,
            Float enabled_radialCenterY,
            float enabled_radialRadius,
            float enabled_width,
            float enabled_height,
            float enabled_marginLeft,
            float enabled_marginTop,
            float enabled_marginRight,
            float enabled_marginBottom,
            float enabled_ringThickness,
            float enabled_ringThicknessRatio,
            float enabled_ringInnerRadius,
            float enabled_ringInnerRadiusRatio,

            @ShapeMode int focused_shapeMode,
            @ColorInt Integer focused_solidColor,
            @ColorInt int focused_strokeColor,
            float focused_strokeWidth,
            float focused_strokeDash,
            float focused_strokeDashGap,
            float focused_radius,
            float focused_radiusLT,
            float focused_radiusLB,
            float focused_radiusRT,
            float focused_radiusRB,
            @ColorInt Integer focused_startColor,
            @ColorInt Integer focused_centerColor,
            @ColorInt Integer focused_endColor,
            @Orientation int focused_orientation,
            @GradientType int focused_gradientType,
            Float focused_radialCenterX,
            Float focused_radialCenterY,
            float focused_radialRadius,
            float focused_width,
            float focused_height,
            float focused_marginLeft,
            float focused_marginTop,
            float focused_marginRight,
            float focused_marginBottom,
            float focused_ringThickness,
            float focused_ringThicknessRatio,
            float focused_ringInnerRadius,
            float focused_ringInnerRadiusRatio,

            @ShapeMode int pressed_shapeMode,
            @ColorInt Integer pressed_solidColor,
            @ColorInt int pressed_strokeColor,
            float pressed_strokeWidth,
            float pressed_strokeDash,
            float pressed_strokeDashGap,
            float pressed_radius,
            float pressed_radiusLT,
            float pressed_radiusLB,
            float pressed_radiusRT,
            float pressed_radiusRB,
            @ColorInt Integer pressed_startColor,
            @ColorInt Integer pressed_centerColor,
            @ColorInt Integer pressed_endColor,
            @Orientation int pressed_orientation,
            @GradientType int pressed_gradientType,
            Float pressed_radialCenterX,
            Float pressed_radialCenterY,
            float pressed_radialRadius,
            float pressed_width,
            float pressed_height,
            float pressed_marginLeft,
            float pressed_marginTop,
            float pressed_marginRight,
            float pressed_marginBottom,
            float pressed_ringThickness,
            float pressed_ringThicknessRatio,
            float pressed_ringInnerRadius,
            float pressed_ringInnerRadiusRatio,

            @ShapeMode int selected_shapeMode,
            @ColorInt Integer selected_solidColor,
            @ColorInt int selected_strokeColor,
            float selected_strokeWidth,
            float selected_strokeDash,
            float selected_strokeDashGap,
            float selected_radius,
            float selected_radiusLT,
            float selected_radiusLB,
            float selected_radiusRT,
            float selected_radiusRB,
            @ColorInt Integer selected_startColor,
            @ColorInt Integer selected_centerColor,
            @ColorInt Integer selected_endColor,
            @Orientation int selected_orientation,
            @GradientType int selected_gradientType,
            Float selected_radialCenterX,
            Float selected_radialCenterY,
            float selected_radialRadius,
            float selected_width,
            float selected_height,
            float selected_marginLeft,
            float selected_marginTop,
            float selected_marginRight,
            float selected_marginBottom,
            float selected_ringThickness,
            float selected_ringThicknessRatio,
            float selected_ringInnerRadius,
            float selected_ringInnerRadiusRatio,

            Drawable drawable,
            Drawable drawable_checked,
            Drawable drawable_checkable,
            Drawable drawable_enabled,
            Drawable drawable_focused,
            Drawable drawable_pressed,
            Drawable drawable_selected
    ) {
        boolean isDefaultNull = false;
        int count = 0;
        Drawable defaultDrawable = drawable != null ? drawable : create(
                shapeMode,
                solidColor,
                strokeColor,
                strokeWidth,
                strokeDash,
                strokeDashGap,
                radius,
                radiusLT,
                radiusLB,
                radiusRT,
                radiusRB,
                startColor,
                centerColor,
                endColor,
                orientation,
                gradientType,
                radialCenterX,
                radialCenterY,
                radialRadius,
                width,
                height,
                marginLeft,
                marginTop,
                marginRight,
                marginBottom,
                ringThickness,
                ringThicknessRatio,
                ringInnerRadius,
                ringInnerRadiusRatio
        );
        if (defaultDrawable != null) {
            count++;
        } else {
            isDefaultNull = true;
        }
        Drawable checkedDrawable = drawable_checked != null ? drawable_checked : create(
                checked_shapeMode,
                checked_solidColor,
                checked_strokeColor,
                checked_strokeWidth,
                checked_strokeDash,
                checked_strokeDashGap,
                checked_radius,
                checked_radiusLT,
                checked_radiusLB,
                checked_radiusRT,
                checked_radiusRB,
                checked_startColor,
                checked_centerColor,
                checked_endColor,
                checked_orientation,
                checked_gradientType,
                checked_radialCenterX,
                checked_radialCenterY,
                checked_radialRadius,
                checked_width,
                checked_height,
                checked_marginLeft,
                checked_marginTop,
                checked_marginRight,
                checked_marginBottom,
                checked_ringThickness,
                checked_ringThicknessRatio,
                checked_ringInnerRadius,
                checked_ringInnerRadiusRatio
        );
        if (checkedDrawable != null) {
            count++;
        }
        Drawable checkableDrawable = drawable_checkable != null ? drawable_checkable : create(
                checkable_shapeMode,
                checkable_solidColor,
                checkable_strokeColor,
                checkable_strokeWidth,
                checkable_strokeDash,
                checkable_strokeDashGap,
                checkable_radius,
                checkable_radiusLT,
                checkable_radiusLB,
                checkable_radiusRT,
                checkable_radiusRB,
                checkable_startColor,
                checkable_centerColor,
                checkable_endColor,
                checkable_orientation,
                checkable_gradientType,
                checkable_radialCenterX,
                checkable_radialCenterY,
                checkable_radialRadius,
                checkable_width,
                checkable_height,
                checkable_marginLeft,
                checkable_marginTop,
                checkable_marginRight,
                checkable_marginBottom,
                checkable_ringThickness,
                checkable_ringThicknessRatio,
                checkable_ringInnerRadius,
                checkable_ringInnerRadiusRatio
        );
        if (checkableDrawable != null) {
            count++;
        }
        Drawable enabledDrawable = drawable_enabled != null ? drawable_enabled : create(
                enabled_shapeMode,
                enabled_solidColor,
                enabled_strokeColor,
                enabled_strokeWidth,
                enabled_strokeDash,
                enabled_strokeDashGap,
                enabled_radius,
                enabled_radiusLT,
                enabled_radiusLB,
                enabled_radiusRT,
                enabled_radiusRB,
                enabled_startColor,
                enabled_centerColor,
                enabled_endColor,
                enabled_orientation,
                enabled_gradientType,
                enabled_radialCenterX,
                enabled_radialCenterY,
                enabled_radialRadius,
                enabled_width,
                enabled_height,
                enabled_marginLeft,
                enabled_marginTop,
                enabled_marginRight,
                enabled_marginBottom,
                enabled_ringThickness,
                enabled_ringThicknessRatio,
                enabled_ringInnerRadius,
                enabled_ringInnerRadiusRatio
        );
        if (enabledDrawable != null) {
            count++;
        }
        Drawable focusedDrawable = drawable_focused != null ? drawable_focused : create(
                focused_shapeMode,
                focused_solidColor,
                focused_strokeColor,
                focused_strokeWidth,
                focused_strokeDash,
                focused_strokeDashGap,
                focused_radius,
                focused_radiusLT,
                focused_radiusLB,
                focused_radiusRT,
                focused_radiusRB,
                focused_startColor,
                focused_centerColor,
                focused_endColor,
                focused_orientation,
                focused_gradientType,
                focused_radialCenterX,
                focused_radialCenterY,
                focused_radialRadius,
                focused_width,
                focused_height,
                focused_marginLeft,
                focused_marginTop,
                focused_marginRight,
                focused_marginBottom,
                focused_ringThickness,
                focused_ringThicknessRatio,
                focused_ringInnerRadius,
                focused_ringInnerRadiusRatio
        );
        if (focusedDrawable != null) {
            count++;
        }
        Drawable pressedDrawable = drawable_pressed != null ? drawable_pressed : create(
                pressed_shapeMode,
                pressed_solidColor,
                pressed_strokeColor,
                pressed_strokeWidth,
                pressed_strokeDash,
                pressed_strokeDashGap,
                pressed_radius,
                pressed_radiusLT,
                pressed_radiusLB,
                pressed_radiusRT,
                pressed_radiusRB,
                pressed_startColor,
                pressed_centerColor,
                pressed_endColor,
                pressed_orientation,
                pressed_gradientType,
                pressed_radialCenterX,
                pressed_radialCenterY,
                pressed_radialRadius,
                pressed_width,
                pressed_height,
                pressed_marginLeft,
                pressed_marginTop,
                pressed_marginRight,
                pressed_marginBottom,
                pressed_ringThickness,
                pressed_ringThicknessRatio,
                pressed_ringInnerRadius,
                pressed_ringInnerRadiusRatio
        );
        if (pressedDrawable != null) {
            count++;
        }
        Drawable selectedDrawable = drawable_selected != null ? drawable_selected : create(
                selected_shapeMode,
                selected_solidColor,
                selected_strokeColor,
                selected_strokeWidth,
                selected_strokeDash,
                selected_strokeDashGap,
                selected_radius,
                selected_radiusLT,
                selected_radiusLB,
                selected_radiusRT,
                selected_radiusRB,
                selected_startColor,
                selected_centerColor,
                selected_endColor,
                selected_orientation,
                selected_gradientType,
                selected_radialCenterX,
                selected_radialCenterY,
                selected_radialRadius,
                selected_width,
                selected_height,
                selected_marginLeft,
                selected_marginTop,
                selected_marginRight,
                selected_marginBottom,
                selected_ringThickness,
                selected_ringThicknessRatio,
                selected_ringInnerRadius,
                selected_ringInnerRadiusRatio
        );
        if (selectedDrawable != null) {
            count++;
        }
        //noinspection StatementWithEmptyBody
        if (count < 1) {
            // impossible，因为该方法被调用说明至少声明了一条属性
        } else {
            boolean needReSetPadding = false;
            if (isDefaultNull || count == 1) {
                // 当设置了margin（非view的margin）时，InsetDrawable会导致view本身的padding失效
                needReSetPadding = true;
                TMP_PADDING[0] = view.getPaddingLeft();
                TMP_PADDING[1] = view.getPaddingTop();
                TMP_PADDING[2] = view.getPaddingRight();
                TMP_PADDING[3] = view.getPaddingBottom();
            }
            if (count == 1 && !isDefaultNull) {
                view.setBackground(defaultDrawable);
            } else {
                ProxyDrawable listDrawable = new ProxyDrawable();
                if (checkedDrawable != null) {
                    listDrawable.addState(new int[]{android.R.attr.state_checked}, checkedDrawable);
                }
                if (checkableDrawable != null) {
                    listDrawable.addState(new int[]{android.R.attr.state_checkable}, checkableDrawable);
                }
                if (enabledDrawable != null) {
                    listDrawable.addState(new int[]{android.R.attr.state_enabled}, enabledDrawable);
                }
                if (focusedDrawable != null) {
                    listDrawable.addState(new int[]{android.R.attr.state_focused}, focusedDrawable);
                }
                if (pressedDrawable != null) {
                    listDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
                }
                if (selectedDrawable != null) {
                    listDrawable.addState(new int[]{android.R.attr.state_selected}, selectedDrawable);
                }
                if (defaultDrawable != null) {
                    listDrawable.addState(new int[]{0}, defaultDrawable);
                } else {
                    Drawable originDrawable = view.getBackground();
                    if (originDrawable != null) {
                        if (originDrawable instanceof ProxyDrawable) {
                            originDrawable = ((ProxyDrawable) originDrawable).getOriginDrawable();
                        }
                        listDrawable.addState(new int[]{0}, originDrawable);
                    }
                }
                view.setBackground(listDrawable);
            }
            if (needReSetPadding) {
                view.setPadding(TMP_PADDING[0], TMP_PADDING[1], TMP_PADDING[2], TMP_PADDING[3]);
            }
        }
    }

    public static Drawable create(
            @ShapeMode int shapeMode, @ColorInt Integer solidColor,
            @ColorInt int strokeColor, @DP float strokeWidth, @DP float strokeDash, @DP float strokeDashGap,
            @DP float radius, @DP float radiusLT, @DP float radiusLB, @DP float radiusRT, @DP float radiusRB,
            @ColorInt Integer startColor, @ColorInt Integer centerColor, @ColorInt Integer endColor,
            @Orientation int orientation, @GradientType int gradientType,
            Float radialCenterX, Float radialCenterY, float radialRadius,
            @DP float width, @DP float height,
            @DP float marginLeft, @DP float marginTop, @DP float marginRight, @DP float marginBottom,
            @DP float ringThickness,
            @DP float ringThicknessRatio,
            @DP float ringInnerRadius,
            @DP float ringInnerRadiusRatio
    ) {
        if (shapeMode == INVALID && solidColor == null && strokeColor == INVALID
                && strokeWidth == INVALID && strokeDash == INVALID && strokeDashGap == INVALID
                && radius == INVALID && radiusLT == INVALID && radiusLB == INVALID
                && radiusRT == INVALID && radiusRB == INVALID && startColor == null
                && centerColor == null && endColor == null && orientation == INVALID
                && gradientType == INVALID && radialCenterX == null && radialCenterY == null
                && radialRadius == INVALID && width == INVALID && height == INVALID
                && marginLeft == INVALID && marginTop == INVALID && marginRight == INVALID && marginBottom == INVALID
        ) {
            // 这里需要判断empty，因为有可能只设置了一个state的drawable，那么其他state的就是empty了
            return null;
        }
        GradientDrawable drawable = new GradientDrawable();
        if (startColor != null && endColor != null) {
            int[] colors;
            if (centerColor != null) {
                colors = new int[3];
                colors[0] = startColor;
                colors[1] = centerColor;
                colors[2] = endColor;
            } else {
                colors = new int[2];
                colors[0] = startColor;
                colors[1] = endColor;
            }
            drawable.setColors(colors);
            drawable.setOrientation(mapOrientation(orientation));
            drawable.setGradientType(gradientType);
            if (gradientType == GradientType.RADIAL) {
                drawable.setGradientCenter(radialCenterX == null ? .5f : radialCenterX,
                        radialCenterY == null ? .5f : radialCenterY);
                drawable.setGradientRadius(dip2px(radialRadius));
            }
        } else {
            if (solidColor != null) {
                drawable.setColor(solidColor);
            }
        }
        drawable.setShape(validShapeMode(shapeMode));
        if (shapeMode == ShapeMode.RING) {
            // 由于GradientDrawable中没有ring相关的公开API，所以使用反射，若对性能有要求，请注意。
            setRingValue(drawable, ringThickness, ringThicknessRatio, ringInnerRadius, ringInnerRadiusRatio);
        }
        if (strokeWidth > 0) {
            drawable.setStroke(dip2px(strokeWidth), strokeColor, dip2px(strokeDash), dip2px(strokeDashGap));
        }
        if (radius <= 0) {
            float[] radiusEach = new float[]{dip2px(radiusLT), dip2px(radiusLT), dip2px(radiusRT), dip2px(radiusRT),
                    dip2px(radiusRB), dip2px(radiusRB), dip2px(radiusLB), dip2px(radiusLB)};
            drawable.setCornerRadii(radiusEach);
        } else {
            drawable.setCornerRadius(dip2px(radius));
        }
        if (width > 0 && height > 0) {
            // https://stackoverflow.com/a/29180660/4698946
            drawable.setSize(dip2px(width), dip2px(height));
        }
        if (marginLeft != 0 || marginTop != 0 || marginRight != 0 || marginBottom != 0) {
            return new InsetDrawable(drawable,
                    dip2px(marginLeft),
                    dip2px(marginTop),
                    dip2px(marginRight),
                    dip2px(marginBottom));
        } else {
            return drawable;
        }
    }

    private static int validShapeMode(@ShapeMode int shapeMode) {
        return shapeMode > ShapeMode.RING || shapeMode < ShapeMode.RECTANGLE
                ? GradientDrawable.RECTANGLE : shapeMode;
    }

    private static GradientDrawable.Orientation mapOrientation(@Orientation int orientation) {
        switch (orientation) {
            case Orientation.BL_TR:
                return GradientDrawable.Orientation.BL_TR;
            case Orientation.BOTTOM_TOP:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case Orientation.BR_TL:
                return GradientDrawable.Orientation.BR_TL;
            case Orientation.LEFT_RIGHT:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case Orientation.RIGHT_LEFT:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case Orientation.TL_BR:
                return GradientDrawable.Orientation.TL_BR;
            case Orientation.TOP_BOTTOM:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case Orientation.TR_BL:
                return GradientDrawable.Orientation.TR_BL;
            default:
                break;
        }
        return GradientDrawable.Orientation.TOP_BOTTOM;
    }

    private static void setRingValue(GradientDrawable drawable,
                                     Float thickness, Float thicknessRatio,
                                     Float innerRadius, Float innerRadiusRatio) {
        try {
            Field mGradientState = drawable.getClass().getDeclaredField("mGradientState");
            mGradientState.setAccessible(true);
            Class mGradientStateClass = mGradientState.get(drawable).getClass();
            Field mUseLevelForShape = mGradientStateClass.getDeclaredField("mUseLevelForShape");
            mUseLevelForShape.setAccessible(true);
            mUseLevelForShape.setBoolean(mGradientState.get(drawable), false);
            if (thickness != null) {
                Field mThickness = mGradientStateClass.getDeclaredField("mThickness");
                mThickness.setAccessible(true);
                mThickness.setInt(mGradientState.get(drawable), dip2px(thickness));
            }
            if (thicknessRatio != null) {
                Field mThicknessRatio = mGradientStateClass.getDeclaredField("mThicknessRatio");
                mThicknessRatio.setAccessible(true);
                mThicknessRatio.setFloat(mGradientState.get(drawable), dip2px(thicknessRatio));
            }
            if (innerRadius != null) {
                Field mInnerRadius = mGradientStateClass.getDeclaredField("mInnerRadius");
                mInnerRadius.setAccessible(true);
                mInnerRadius.setInt(mGradientState.get(drawable), dip2px(innerRadius));
            }
            if (innerRadiusRatio != null) {
                Field mInnerRadiusRatio = mGradientStateClass.getDeclaredField("mInnerRadiusRatio");
                mInnerRadiusRatio.setAccessible(true);
                mInnerRadiusRatio.setFloat(mGradientState.get(drawable), dip2px(innerRadiusRatio));
            }
        } catch (NoSuchFieldException | IllegalAccessException t) {
            t.printStackTrace();
        }
    }

    private static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + .5f);
    }


    @IntDef({
            ShapeMode.RECTANGLE,
            ShapeMode.OVAL,
            ShapeMode.LINE,
            ShapeMode.RING,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShapeMode {
        int RECTANGLE = GradientDrawable.RECTANGLE;
        int OVAL = GradientDrawable.OVAL;
        /**
         * 画线时，有几点特性必须要知道的：
         * 1. 只能画水平线，画不了竖线；
         * 2. 线的高度是通过stroke的android:width属性设置的；
         * 3. size的android:height属性定义的是整个形状区域的高度；
         * 4. size的height必须大于stroke的width，否则，线无法显示；
         * 5. 线在整个形状区域中是居中显示的；
         * 6. 线左右两边会留有空白间距，线越粗，空白越大；
         * 7. 引用虚线的view需要添加属性android:layerType，值设为"software"，否则显示不了虚线。
         */
        int LINE = GradientDrawable.LINE;
        int RING = GradientDrawable.RING;
    }

    @IntDef({
            GradientType.LINEAR,
            GradientType.RADIAL,
            GradientType.SWEEP
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface GradientType {
        int LINEAR = 0;
        int RADIAL = 1;
        int SWEEP = 2;
    }

    @IntDef({
            Orientation.TOP_BOTTOM,
            Orientation.TR_BL,
            Orientation.RIGHT_LEFT,
            Orientation.BR_TL,
            Orientation.BOTTOM_TOP,
            Orientation.BL_TR,
            Orientation.LEFT_RIGHT,
            Orientation.TL_BR
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
        int TOP_BOTTOM = 0;
        int TR_BL = 1;
        int RIGHT_LEFT = 2;
        int BR_TL = 3;
        int BOTTOM_TOP = 4;
        int BL_TR = 5;
        int LEFT_RIGHT = 6;
        int TL_BR = 7;
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target({PARAMETER, FIELD})
    @interface DP {
    }

    public static class ProxyDrawable extends StateListDrawable {

        private Drawable originDrawable;

        @Override
        public void addState(int[] stateSet, Drawable drawable) {
            if (stateSet != null && stateSet.length == 1 && stateSet[0] == 0) {
                originDrawable = drawable;
            }
            super.addState(stateSet, drawable);
        }

        Drawable getOriginDrawable() {
            return originDrawable;
        }
    }
}
