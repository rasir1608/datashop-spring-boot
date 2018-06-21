package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DProject;
import com.datashop.exception.DatashopException;
import com.datashop.server.inter.ProjectServer;
import com.datashop.utils.FileHandler;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rasir on 2018/5/29.
 */
@RestController
@RequestMapping("/dproject")
public class projectController {

    @Autowired
    private ProjectServer projectServer;

    /**
     * 新建和保存项目
     * @param dProject
     * @return
     */
    @PostMapping("/save")
    public Map save(@RequestBody DProject dProject){
        if(dProject.getId() == null){
            return create(dProject);
        } else {
            return update(dProject);
        }
    }

    private Map create(DProject dProject){
        DProject exit = projectServer.findByName(dProject.getName());
        if(exit != null) {
            throw new DatashopException("已有同名项目存在，请重新命名后提交！",401);
        } else {
            dProject.setCreateTime(new Date().getTime());
            dProject.setUpdateTime(new Date().getTime());
            return ResultUtil.handleResult(projectServer.create(dProject),"项目信息保存失败！",500);
        }
    }

    private Map update(DProject dProject){
        DProject exit = projectServer.findById(dProject.getId());
        if(exit == null) {
            throw new DatashopException("要更新的项目不存在！",404);
        } else if(!dProject.getName().equals(exit.getName())){
            throw new DatashopException("暂不支持修改项目名称，请确认后再保存！",401);
        } else {
            dProject.setUpdateTime(new Date().getTime());
            return ResultUtil.handleResult(projectServer.update(dProject),"项目信息保存失败！",500);
        }
    }

    /**
     * 获取所有的项目
     * @param req
     * @return
     */
    @PostMapping("/page")
    public Map page(@RequestBody JSONObject req){
        return ResultUtil.handleResult(projectServer.page(req),"项目的分页信息获取失败",500);
    }

    /**
     * 删除项目
     * @param projectId
     * @return
     */
    @GetMapping("/delete/{projectId}")
    public Map deleteProjectById(@PathVariable Integer projectId){
        return ResultUtil.handleResult(projectServer.deleteById(projectId),"项目删除失败",500);
    }

    /**
     * 获取项目详情
     * @param projectId
     * @return
     */
    @GetMapping("/detail/{projectId}")
    public Map getProjectDetail(@PathVariable Integer projectId){
        return ResultUtil.handleResult(projectServer.queryDetail(projectId),"未找到项目",500);
    }

    /**
     * 上传项目原型图片
     * @param multipartFile
     * @param projectId
     * @return
     */
    @PostMapping("/uploadModelImg/{projectId}")
    public Map uploadModelPic(@RequestParam("file") MultipartFile multipartFile,@PathVariable Integer projectId){
        DProject dP = projectServer.findById(projectId);
        if(dP != null){
            String oldModel = dP.getModel();
            FileHandler.removeFile(oldModel);
            String fileName = dP.getName()+"-model-"+new Date().getTime();
            String filePath = savePic(multipartFile,2097152,fileName);
            if(filePath != null){
                return ResultUtil.success(filePath);
            } else {
                throw new DatashopException("原型图片保存失败",500);
            }
        } else {
            throw new DatashopException("项目ID异常，请确认后查询",404);
        }
    }


    /**
     * 上传UI图片
     * @param multipartFile
     * @param projectId
     * @return
     */
    @PostMapping("/uploadUiImg/{projectId}")
    public Map uploadUPic(@RequestParam("file") MultipartFile multipartFile,@PathVariable Integer projectId){
        DProject dP = projectServer.findById(projectId);
        if(dP != null){
            String oldUi = dP.getUi();
            FileHandler.removeFile(oldUi);
            String fileName = dP.getName()+"-ui-"+new Date().getTime();
            String filePath = savePic(multipartFile,2097152,fileName);
            if(filePath != null){
                return ResultUtil.success(filePath);
            } else {
                throw new DatashopException("UI图片保存失败",500);
            }
        } else {
            throw new DatashopException("项目ID异常，请确认后查询",404);
        }
    }



    /**
     * 获取文件的后缀
     * @param originFileName
     * @return
     */
    private String getFileSuffix(String originFileName){
        String[] nameArr = originFileName.split("\\.");
        if(nameArr.length < 2) {
            return null;
        } else {
            return nameArr[nameArr.length -1];
        }
    }

    /**
     * 保存图片
     * @param multipartFile
     * @param size
     * @param name
     * @return
     */
    private String savePic(MultipartFile multipartFile,int size,String name){
        if(multipartFile.isEmpty()){
            throw new DatashopException("未获取到图片",401);
        } else if(multipartFile.getSize() > size){
            throw new DatashopException("请选择小于2MB的图片",401);
        }

        String originFileName = multipartFile.getOriginalFilename();
        String suffix = getFileSuffix(originFileName);

        if(suffix == null) {
            throw new DatashopException("文件名后缀错误！",401);
        } else {
            List<String> suffixArray = Arrays.asList("png","jpg","gif");
            if(!suffixArray.contains(suffix)){
                throw new DatashopException("请上传png、jpg、gif类型的文件！",401);
            }
        }
        String dirName = "/image";
        String fileName = name +"."+suffix;
        try{
            String filePath = FileHandler.saveFile(multipartFile.getInputStream(),fileName,dirName);
            return filePath;
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }


}
