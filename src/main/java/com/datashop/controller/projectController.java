package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DPowerMapping;
import com.datashop.domain.DProject;
import com.datashop.exception.DatashopException;
import com.datashop.server.inter.PowerMappingServer;
import com.datashop.server.inter.ProjectServer;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.FileHandler;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
            throw new DatashopException("已有同名项目存在，请重新命名后提交！",200);
        } else {
            dProject.setCreateTime(new Date().getTime());
            dProject.setUpdateTime(new Date().getTime());
            DPowerMapping dpm = new DPowerMapping();
            dpm.setPower(5);
            Map cookie = CookieUtil.getCookie("bear",Map.class);
            dpm.setUserId(new Integer((String) cookie.get("userId")));
            dpm.setCreateTime(new Date().getTime());
            dpm.setUpdateTime(new Date().getTime());
            return ResultUtil.success(projectServer.create(dProject,dpm));
        }
    }

    private Map update(DProject dProject){
        DProject exit = projectServer.findById(dProject.getId());
        if(exit == null) {
            throw new DatashopException("要更新的项目不存在！",200);
        } else if(!dProject.getName().equals(exit.getName())){
            throw new DatashopException("暂不支持修改项目名称，请确认后再保存！",200);
        } else {
            dProject.setUpdateTime(new Date().getTime());
            return ResultUtil.handleResult(projectServer.update(dProject),"项目信息保存失败！",200);
        }
    }

    /**
     * 获取所有的项目
     * @param req
     * @return
     */
    @PostMapping("/page")
    public Map page(@RequestBody JSONObject req){
        String name = req.getString("name");
        if(name != null) {
            name = "%"+name+"%";
            req.put("name",name);
        }
        String creatorName = req.getString("creatorName");
        if(creatorName != null) {
            creatorName = "%"+creatorName+"%";
            req.put("creatorName",creatorName);
        }
        Integer size = req.getInteger("size");
        Integer currentPage = req.getInteger("currentPage");
        req.put("offset",(currentPage-1)*size);
        Integer userId = CookieUtil.getUserId("bear");
        req.put("userId",userId);
        return ResultUtil.handleResult(projectServer.page(req),"项目的分页信息获取失败",200);
    }

    /**
     * 删除项目
     * @param projectId
     * @return
     */
    @DeleteMapping("/{projectId}")
    public Map deleteProjectById(@PathVariable Integer projectId){
        return ResultUtil.handleResult(projectServer.deleteById(projectId),"项目删除失败",200);
    }

    /**
     * 获取项目详情
     * @param projectId
     * @return
     */
    @GetMapping("/{projectId}")
    public Map getProjectDetail(@PathVariable Integer projectId){
        Integer userId = CookieUtil.getUserId("bear");
        return ResultUtil.handleResult(projectServer.queryDetail(projectId,userId),"未找到项目",200);
    }

    /**
     * 上传项目原型、ui、web图片
     * @param multipartFile
     * @param kind
     * @param projectId
     * @return
     */
    @PostMapping("/upload/{kind}/{projectId}")
    public Map uploadModelPic(@RequestParam("file") MultipartFile multipartFile,@PathVariable String kind,@PathVariable Integer projectId){
        DProject dp = projectServer.findById(projectId);
        if(dp != null){
            String fileName = dp.getName()+"-"+kind+"-"+new Date().getTime();
            String filePath = savePic(multipartFile,2097152,fileName);
            if(filePath != null){
                String oldKind = null;
                switch (kind){
                    case "model":
                        oldKind = dp.getModel();
                        dp.setModel(filePath);
                        break;
                    case "ui":
                        oldKind = dp.getUi();
                        dp.setUi(filePath);
                        break;
                    case "web":
                        oldKind = dp.getWeb();
                        dp.setWeb(filePath);
                        break;
                    default:
                        break;
                }
                FileHandler.removeFile(oldKind);
                Integer userId = CookieUtil.getUserId("bear");
                dp.setModifier(userId);
                dp.setUpdateTime(new Date().getTime());
                projectServer.update(dp);
                return ResultUtil.success(filePath);
            } else {
                throw new DatashopException("图片保存失败",200);
            }
        } else {
            throw new DatashopException("项目ID异常，请确认后查询",200);
        }
    }

    @PostMapping("/updateUrl")
    public Map uploadUrl(@RequestBody JSONObject req){
        Integer proId = req.getInteger("projectId");
        String kind = req.getString("tag");
        String newUrl = req.getString("url");
        DProject dp = projectServer.findById(proId);
        if(dp == null) {
            throw new DatashopException("项目未找到，请刷新后重试！",200);
        } else {
            String oldUrl = null;
            switch (kind){
                case "model":
                    oldUrl = dp.getModel();
                    dp.setModel(newUrl);
                    break;
                case "ui":
                    oldUrl = dp.getUi();
                    dp.setUi(newUrl);
                    break;
                case "web":
                    oldUrl = dp.getWeb();
                    dp.setWeb(newUrl);
                    break;
                default:
                    break;
            }
            FileHandler.removeFile(oldUrl);
            Integer userId = CookieUtil.getUserId("bear");
            dp.setModifier(userId);
            dp.setUpdateTime(new Date().getTime());
            return ResultUtil.success(projectServer.update(dp));
        }
    }

//    /**
//     * 上传UI图片
//     * @param multipartFile
//     * @param projectId
//     * @return
//     */
//    @PostMapping("/upload/ui/{projectId}")
//    public Map uploadUiPic(@RequestParam("file") MultipartFile multipartFile,@PathVariable Integer projectId){
//        DProject dP = projectServer.findById(projectId);
//        if(dP != null){
//            String oldUi = dP.getUi();
//            FileHandler.removeFile(oldUi);
//            String fileName = dP.getName()+"-ui-"+new Date().getTime();
//            String filePath = savePic(multipartFile,2097152,fileName);
//            if(filePath != null){
//                return ResultUtil.success(filePath);
//            } else {
//                throw new DatashopException("UI图片保存失败",200);
//            }
//        } else {
//            throw new DatashopException("项目ID异常，请确认后查询",200);
//        }
//    }
//
//    @PostMapping("/upload/web/{projectId}")
//    public Map uploadWebPic(@RequestParam("file") MultipartFile multipartFile,@PathVariable Integer projectId){
//        DProject dP = projectServer.findById(projectId);
//        if(dP != null){
//            String oldWeb = dP.getWeb();
//            FileHandler.removeFile(oldWeb);
//            String fileName = dP.getName()+"-web-"+new Date().getTime();
//            String filePath = savePic(multipartFile,2097152,fileName);
//            if(filePath != null){
//                return ResultUtil.success(filePath);
//            } else {
//                throw new DatashopException("web图片保存失败",200);
//            }
//        } else {
//            throw new DatashopException("项目ID异常，请确认后查询",200);
//        }
//    }



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
            throw new DatashopException("未获取到图片",200);
        } else if(multipartFile.getSize() > size){
            throw new DatashopException("请选择小于2MB的图片",200);
        }

        String originFileName = multipartFile.getOriginalFilename();
        String suffix = getFileSuffix(originFileName);

        if(suffix == null) {
            throw new DatashopException("文件名后缀错误！",200);
        } else {
            List<String> suffixArray = Arrays.asList("png","jpg","gif");
            if(!suffixArray.contains(suffix)){
                throw new DatashopException("请上传png、jpg、gif类型的文件！",200);
            }
        }
        String dirName = "/image";
        String fileName = name +"."+suffix;
        try{
            String filePath = FileHandler.saveFile(multipartFile.getInputStream(),fileName,dirName);
            return filePath;
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),200);
        }
    }


}
