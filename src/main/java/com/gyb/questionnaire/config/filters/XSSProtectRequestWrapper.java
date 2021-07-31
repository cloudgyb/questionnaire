package com.gyb.questionnaire.config.filters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * XSS Protection Request Wrapper
 * 获取参数时，对html标签进行转义
 *
 * @author cloudgyb
 * @since 2021/7/31 16:14
 */
public class XSSProtectRequestWrapper extends HttpServletRequestWrapper {

    public XSSProtectRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        return encodeParameter(parameter);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        final Map<String, String[]> parameterMap = super.getParameterMap();
        if (parameterMap == null)
            return null;
        final HashMap<String, String[]> encodedParameterMap = new HashMap<>();
        final Set<String> keys = parameterMap.keySet();
        for (String key : keys) {
            final String[] params = parameterMap.get(key);
            final String[] encodeParams = encodeParameters(params);
            encodedParameterMap.put(key, encodeParams);
        }
        return encodedParameterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        final String[] parameterValues = super.getParameterValues(name);
        return encodeParameters(parameterValues);
    }

    private String[] encodeParameters(String[] parameters) {
        if (parameters == null || parameters.length == 0)
            return parameters;
        final String[] encodedParams = new String[parameters.length];
        int i = 0;
        for (String parameter : parameters) {
            final String encodedParam = encodeParameter(parameter);
            encodedParams[i++] = encodedParam;
        }
        return encodedParams;
    }

    private String encodeParameter(String parameter) {
        if (parameter == null)
            return null;
        parameter = parameter.replace("&", "&amp;");
        parameter = parameter.replace(" ", "&nbsp;");
        parameter = parameter.replace("<", "&lt;");
        parameter = parameter.replace(">", "&gt;");
        parameter = parameter.replace("\"", "&quot;");
        parameter = parameter.replace("'", "&apos;");
        return parameter;
    }
}
