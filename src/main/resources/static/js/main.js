$(document).ready(function(){

    fontColor = "0.0.0";
    var canvas = document.getElementById('canvas');
    var stage = new JTopo.Stage(canvas);
    showJTopoToobar(stage);
    var scene = new JTopo.Scene(stage);
    scene.background = './img/bg.jpg';
    // scene.background = './img/a.png';

    // text = "点击节点可连线(连个节点)";
    text = "";
    var msgNode = new JTopo.TextNode(text);
    msgNode.fontColor = fontColor;
    msgNode.dragable = false;
    msgNode.selected = false;
    msgNode.zIndex++;
    msgNode.setBound(25, 25);
    scene.add(msgNode);

    var beginNode = null;
    var endNode = null;
    var tempNodeA = new JTopo.Node('tempA');
    tempNodeA.setSize(1, 1);
    var tempNodeZ = new JTopo.Node('tempZ');
    tempNodeZ.setSize(1, 1);

    var textfield = $("#jtopo_textfield");

    node_info = {};
    link_info = {};
    component_count = {};
    componentData = null;
    currentNode = null;
    currentLink = null;

    function fill_table(name, data) {
        $("#parameterTable").html('<tr> <th>名称</th> <th>类型</th> <th>参数值</th></tr>');
        if (name == null && data == null) {
            $("#current_name").html("");
            return;
        }
        $("#current_name").html(name);
        for (key in data["parameters"]){
            ele = data['parameters'][key];
            $("#parameterTable").append(
                $(
                    '<tr>' +
                    '<td class="">' + key + '</td>' +
                    '<td class="">' + ele.type + '</td>' +
                    '<td>' + '<input type="text"  class="form-control" style="width:60px;"value=' + ele.value + '></td>' +
                    '</tr>'
                )
            )
        }
        $('#component_name').val(data['name']);
        $('#component_description').val(data['description']);
        $('#component_version').val(data['version']);
        $('#component_owner').val(data['owner']);
        $('#component_created_time').val(data['created_time']);
        $('#component_input').val(Object.keys(data['input'])),
            $('#component_output').val(Object.keys(data['output']))
    }

    function fill_link(data) {
        $("#link_from").val(data['from'].text);
        $("#link_to").val(data['to'].text);
        $("#link_input").val(data['input']);
        $("#link_output").val(data['output']);
    }

    function initComponent() {
        $("#link").hide();
        $("#componentParameters").hide();
        $("#link_set").hide();
        $.getJSON("component.json").done(function(data){
            componentData = data;
            for(var key in componentData){
                ele = $('<input type="button" class="btn btn-info" value=""/><br/>');
                ele.val(key);
                component_count[key] = 1;
                ele.click(function(event) {
                        var value = $(event.target).val();
                        name = value + '_' + component_count[value];
                        fill_table(name, componentData[value]);
                        var node = new JTopo.Node(name);
                        node.setImage("./img/component.png");
                        node.setSize(60, 60);
                        node.fontColor = fontColor;
                        component_count[value] += 1;
                        node.addEventListener('mouseup', function (event) {
                            currentNode = this;
                        });
                        node.setLocation(300 + Math.random() * 300, 200 + Math.random() * 200);
                        scene.add(node);
                        node_info[name] = componentData[value];
                        beginNode = null;
                        currentNode = node;
                    }
                );
                $("#components").append(ele);
            }
        });
    }

    function removeLink(link) {
        scene.remove(link);
        scene.remove(link);
        from = link.nodeA;
        to = link.nodeZ;
        node_info[from.text]["output"][link_info[name]["input"]] = true;
        node_info[to.text]["input"][link_info[name]["output"]] = true;
        delete link_info[from.text + ":" + to.text];
        $("#link").hide();
    }

    function newLink(nodeA, nodeZ, dashedPattern){
        var link = new JTopo.Link(nodeA, nodeZ);
        link.lineWidth = 3; // 线宽
        // link.dashedPattern = dashedPattern; // 虚线
        link.arrowsRadius = 15; //箭头大小
        link.bundleOffset = 60; // 折线拐角处的长度
        link.bundleGap = 20; // 线条之间的间隔
        link.textOffsetY = 3; // 文本偏移量（向下3个像素）
        link.strokeColor = '0,200,255';
        scene.add(link);
        return link;
    }

    initComponent();

    $("#link_confirm").click(function () {
        beginNode = currentLink.nodeA;
        endNode = currentLink.nodeZ;
        name = beginNode.text + ":" + endNode.text;
        link_info[name] = {
            "object" : currentLink,
            "from" : beginNode,
            "to": endNode,
            "input": $("#input_select").val(),
            "output": $("#output_select").val()
        };
        node_info[beginNode.text]["output"][link_info[name]["input"]] = false;
        node_info[endNode.text]["input"][link_info[name]["output"]] = false;
        fill_link(link_info[name]);
        scene.add(currentLink);
        beginNode = null;
        endNode = null;
        $("#input_select").html("");
        $("#output_select").html("");
        $("#link_set").hide();
        fill_link(link_info[name]);
        $("#link").show();

    });

    scene.mouseup(function(e){
        if(e.button == 2){
            currentNode = null;
            beginNode = null;
            return;
        }
        if(e.target != null && e.target instanceof JTopo.Node){
            if (e.target.text == text)
                return;
            $("#componentInfo").show();
            $("#componentParameters").show();
            $("#link").hide();
            $("#link_set").hide();
            fill_table(e.target.text, node_info[e.target.text]);
            currentNode = e.target;
            if(beginNode == null){
                beginNode = e.target;
                tempNodeA.setLocation(e.x, e.y);
                tempNodeZ.setLocation(e.x, e.y);
            }else if(beginNode !== e.target){
                var endNode = e.target;
                currentLink = newLink(beginNode, endNode, '', 'Arrow');
                for(var key in node_info[beginNode.text]['output']) {
                    if (node_info[beginNode.text]['output'][key])
                        $("#input_select").append($("<option>" + key +"</option>"));
                    else
                        $("#input_select").append($("<option disabled>" + key +"</option>"));
                }
                for(var key in node_info[endNode.text]['input']) {
                    if (node_info[endNode.text]['input'][key])
                        $("#output_select").append($("<option>" + key +"</option>"));
                    else
                        $("#output_select").append($("<option disabled>" + key +"</option>"));
                }
                $("#link_set").show();
                $("#componentParameters").hide();
                alert("请在左侧选择输入输出，并确定");
            }else{
                beginNode = null;
            }
        } else if (e.target instanceof JTopo.Link) {
            currentLink = e.target;
            name = currentLink.nodeA.text + ":" + currentLink.nodeZ.text;
            fill_link(link_info[name]);
            $("#componentParameters").hide();
            $("#componentInfo").hide();
            $("#link_set").hide();
            $("#link").show();
        }else{
            $("#componentParameters").hide();
            $("#componentInfo").hide();
            $("#link").hide();
            $("#link_set").hide();
            currentNode = null;
            beginNode = null;
        }
    });

    scene.mousedown(function(e){
        if(e.target == null || e.target === beginNode){
            // scene.remove(link);
        }
    });

    scene.mousemove(function(e){
        tempNodeZ.setLocation(e.x, e.y);
    });

    $("#link_delete").click(function () {
        removeLink(currentLink);
        currentLink = null;
    });

    $("#jtopo_textfield").blur(function(){
        textfield[0].JTopoNode.text = textfield.hide().val();
    });

    stage.click(function(event){
        if(event.button == 0){
            return;
        }
    });

    $("#delete").click(function(){
        elements = scene.getDisplayedElements();
        elements.forEach(function(val, index, arr){
            if(val instanceof JTopo.Link && (val.nodeA == currentNode || val.nodeZ == currentNode))
                removeLink(val);
        });
        scene.remove(currentNode);
        delete node_info[currentLink.text];
        currentNode = null;
        beginNode = null;
        $("#componentParameters").hide();
    });

    $("#parameterSubmit").click(function () {
        parameters = {};
        tableObj = $("#parameterTable")[0];
        for (var i = 1; i < tableObj.rows.length; i++) {
            parameters[tableObj.rows[i].cells[0].innerText] = {
                "type": tableObj.rows[i].cells[1].innerText,
                "value": $(tableObj.rows[i].cells[2]).children().val()
            }
        };
        if(currentNode != null) {
            node_info[currentNode.text]["parameters"] = parameters;
        }
    });


    $("#run").click(function () {
        console.log(node_info);
        console.log(link_info);
        var links = {};
        for (var item in link_info) {
            links[item] = {'input': link_info[item]['input'],'output': link_info[item]['output']}
        }

        info = {
            "nodes" : JSON.stringify(node_info),
            "links" : JSON.stringify(links)
        };
        $.post("/run",info,function(resultJSONObject){
            if(resultJSONObject.success){
                $.messager.alert("发送成功");
            }else{
                $.messager.alert("失败");
            }
        },"json");
    });
});