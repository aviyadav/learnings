package com.scireum.open.xml;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import com.scireum.open.commons.Value;

public interface StructuredNode {

    StructuredNode queryNode(String xpath) throws XPathExpressionException;

    List<StructuredNode> queryNodeList(String xpath) throws XPathExpressionException;

    StructuredNode[] queryNodes(String path) throws XPathExpressionException;

    String queryString(String path) throws XPathExpressionException;

    Value queryValue(String path) throws XPathExpressionException;

    boolean isEmpty(String path) throws XPathExpressionException;

    String getNodeName();
}
