package com.chuangdu.pad.module.web.contract;

import com.chuangdu.library.annotation.FragmentScrop;
import com.chuangdu.pad.common.dagger.AppComponent;
import com.chuangdu.pad.module.web.BrowserActivity;

import dagger.Component;

/**
 * @author sc
 * @since 2020-03-26
 */
@FragmentScrop
@Component(dependencies = AppComponent.class,
        modules = BrowserModule.class)
public interface BrowserComponent {
    void inject(BrowserActivity activity);
}
