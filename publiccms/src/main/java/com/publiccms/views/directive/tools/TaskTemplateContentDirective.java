package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.component.file.FileComponent;
import com.sanluan.common.handler.RenderHandler;

@Component
public class TaskTemplateContentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        if (notEmpty(path)) {
            handler.put("object", fileComponent.getFileContent(siteComponent.getTaskTemplateFilePath(getSite(handler), path)))
                    .render();
        }
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private FileComponent fileComponent;
}
