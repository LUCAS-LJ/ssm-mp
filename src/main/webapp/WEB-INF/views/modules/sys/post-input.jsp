<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    var organ_combotree;
    $(function(){
        loadOrgan();
    });

    function loadOrgan(){
        organ_combotree = $("#organId").combotree({
            url:'${ctxAdmin}/sys/organ/tree?selectType=select',
            multiple:false,//是否可多选
            editable:false
        });
    }
</script>
<div>
    <form id="post_form" class="dialog-form" method="post" novalidate>
        <input type="hidden"  name="id" />
        <!-- 用户版本控制字段 version -->
        <input type="hidden" id="version" name="version" />
        <div>
            <label>机构:</label>
            <input id="organId" name="organId" validType="combotreeRequired['#organId']" style="width: 260px;height: 28px;">
        </div>
        <div>
            <label>岗位名称:</label>
            <input name="name" type="text" class="easyui-validatebox textbox"
                   maxLength="100" data-options="required:true,missingMessage:'请输入岗位名称.',validType:['minLength[1]','legalInput']">
        </div>
        <div>
            <label>岗位编码:</label>
            <input name="code" type="text" class="easyui-validatebox textbox"
                   maxLength="36" >
        </div>
        <div>
            <label style="vertical-align: top;">描述:</label>
            <%--<textarea maxLength="255" name="remark"--%>
                      <%--style="position: relative; resize: none; height: 75px; width: 260px;"></textarea>--%>
            <input name="remark" maxLength="255" class="easyui-textbox" maxLength="2000"  data-options="multiline:true" style="width:260px;height:75px;">
        </div>
    </form>
</div>