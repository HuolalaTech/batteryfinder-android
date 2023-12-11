package cn.huolala.plugin


import com.android.build.gradle.AppExtension
import cn.huolala.plugin.core.config.BatteryConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

class BatteryPlugin implements Plugin<Project> {

    Project project

    @Override
    void apply(Project target) {
        project = target
        def android = project.extensions.getByType(AppExtension)
        project.extensions.create("BatteryHookConfig", BatteryConfig.class)
        android.registerTransform(new BatteryTransform(project), Collections.EMPTY_LIST)
        project.tasks.create('printBatteryConfig').doFirst {
            System.out.println("battery config is  " + project.BatteryHookConfig)
        }
    }
}


