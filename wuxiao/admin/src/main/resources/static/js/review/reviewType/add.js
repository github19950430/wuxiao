var $;
var $form;
var form;
layui.use(['element', 'form', 'laydate', 'layer', 'upload'], function () {
	var layer = parent.layer === undefined ? layui.layer : parent.layer,
		$ = layui.jquery;
		form = layui.form;
		 form.on('select(parentId)', function (data) {
			 $("#parentName").val(data.elem[data.elem.selectedIndex].text);
		 });

	
})