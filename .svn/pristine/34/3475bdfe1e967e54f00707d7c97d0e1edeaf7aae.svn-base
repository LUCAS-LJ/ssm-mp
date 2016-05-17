/*@版权所有LIJIANG*/ 
package com.ssm.pay.modules.sys.web;

import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ssm.pay.common.model.SsmResult;
import com.ssm.pay.common.utils.mapper.SsmJsonMapper;
import com.ssm.pay.common.web.springmvc.SsmSimpleController;
import com.ssm.pay.common.web.utils.SsmMediaTypes;
import com.ssm.pay.modules.sys.service.SsmCommonManager;

/** 提供公共方法控制器
* @author Jiang.Li
* @version 2016年4月15日 上午10:39:03
*/
@Controller
@RequestMapping("${adminPath}/common")
public class SsmCommonController extends SsmSimpleController {
	@Autowired
    private SsmCommonManager commonManager;

    /**
     * 字段校验
     *
     * @param entityName 实体类名称 例如: "Resource"
     * @param fieldName  属性名称
     * @param fieldValue 属性值
     * @param rowId      主键ID
     * @return
     */
    @RequestMapping("fieldCheck")
    @ResponseBody
    public SsmResult fieldCheck(String entityName, String fieldName, String fieldValue, Long rowId) {
        Long entityId = commonManager.getIdByProperty(entityName, fieldName,
                fieldValue);
        boolean isCheck = true;// 是否通过检查
        if (entityId != null) {
            if (rowId != null) {
                if (!rowId.equals(entityId)) {
                    isCheck = false;
                }
            } else {
                isCheck = false;
            }

        }
        return new SsmResult(SsmResult.SUCCESS, null, isCheck);
    }

    /**
     * JsonP跨域输出示例
     * @param callbackName 回调方法
     * @return
     */
    @RequestMapping(value = "mashup", produces = SsmMediaTypes.JAVASCRIPT_UTF_8)
    @ResponseBody
    public String mashup(@RequestParam("callback") String callbackName) {

        // 设置需要被格式化为JSON字符串的内容.
        Map<String, String> map = Collections.singletonMap("content", "<p>你好，世界！</p>");

        // 渲染返回结果.
        return SsmJsonMapper.getInstance().toJsonP(callbackName, map);
    }
}
