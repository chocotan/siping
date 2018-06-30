package io.loli.siping.client;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * Created by yewenlin on 2016-8-16.
 */
public class SipingConfigurationSelector extends AdviceModeImportSelector<EnableSiping> {
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{SipingConfiguration.class.getName()};
    }
}