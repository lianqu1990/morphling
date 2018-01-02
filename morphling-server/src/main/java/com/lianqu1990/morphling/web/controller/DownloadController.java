package com.lianqu1990.morphling.web.controller;

import com.lianqu1990.morphling.consts.DeployConst;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author hanchao
 * @date 2017/12/18 10:09
 */
@RequestMapping("/download")
@Controller
public class DownloadController {

    @GetMapping("/{projectName}_{tag}.tar.gz")
    public ResponseEntity<byte[]> downloadPackage(@PathVariable String projectName,@PathVariable String tag) throws IOException {
        String fileName = projectName+"_"+tag+".tar.gz";
        String packagePath = DeployConst.PACKAGE_HOME+projectName+"/"+fileName;
        File download = new File(packagePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(download.length());
        headers.setContentDispositionFormData(fileName,null, Charset.forName("ISO-8859-1"));
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(download),headers, HttpStatus.CREATED);
    }
}
