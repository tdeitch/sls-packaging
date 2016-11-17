/*
 * Copyright 2016 Palantir Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.gradle.javadist.tasks

import com.palantir.gradle.javadist.JavaDistributionPlugin
import com.palantir.gradle.javadist.util.EmitFiles
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

class CreateManifestTask extends DefaultTask {

    @Input
    String serviceName

    @Input
    String serviceGroup

    CreateManifestTask() {
        group = JavaDistributionPlugin.GROUP_NAME
        description = "Generates a simple yaml file describing the package content."
    }

    @Input
    public String getProjectVersion() {
        return String.valueOf(project.version)
    }

    @OutputFile
    File getManifestFile() {
        return new File("${project.buildDir}/deployment/manifest.yml")
    }

    @TaskAction
    void createManifest() {
        EmitFiles.replaceVars(
                CreateManifestTask.class.getResourceAsStream('/manifest.yml'),
                getManifestFile().toPath(),
                ['@serviceGroup@'  : serviceGroup,
                 '@serviceName@'   : serviceName,
                 '@serviceVersion@': getProjectVersion()])
    }

    public void configure(String serviceName, String serviceGroup) {
        this.serviceName = serviceName
        this.serviceGroup = serviceGroup
    }
}
