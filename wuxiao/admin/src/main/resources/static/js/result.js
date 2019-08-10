//相关成员
	var members=[];
	var memHtml="<div class='top6ItemBox bb resultMember'>";
		memHtml+="<div class='top6ItemBox_item br itemtah'>";
		memHtml+="<input type='text' id='memberN' class='layui-input'>";
		memHtml+="</div>";
		memHtml+="<div class='top6ItemBox_item br itemtah'>";
		memHtml+="<select id='memberS' lay-verify='required' class='xbselect itemtah'><option value='男'>男</option><option value='女'>女</option></select>";
		memHtml+="</div>";
		memHtml+="<div class='top6ItemBox_item br itemtah'>";
		memHtml+="<input type='text' id='memberB' class='layui-input' id='date1'>";
		memHtml+="</div>";
		memHtml+="<div class='top6ItemBox_item br itemtah'>";
		memHtml+="<input type='text' id='memberE'  class='layui-input'>";
		memHtml+="</div>";
		memHtml+="<div class='top6ItemBox_item br itemtah'>";
		memHtml+="<input type='text' id='memberT'  class='layui-input'>";
		memHtml+="</div>";
		memHtml+="<div class='top6ItemBox_item br itemtah'>";
		memHtml+="<input type='text' id='memberD'   class='layui-input'>";
		memHtml+="</div>";
		memHtml+="<div class='top6ItemBox_item br itemtah' style='width: 12.5%'>";
		memHtml+="<input type='text' id='memberW'  class='layui-input'>";
		memHtml+="</div>";
		memHtml+="<div class='top6ItemBox_item  itemtah' style='width: 12.5%'>";
		memHtml+="<span class='top6ItemBox_item_delete' id='' onClick='delMember(this)'>删除</span>";
		memHtml+="</div>";
		memHtml+="</div>";

	//增加成员;
	$('.addimgBtn').on('click', function() {
		$('.top6').append(memHtml);
	});

	//删除成员
	function delMember(This){
		var id=$(This).attr("id");
		var dom=$(This).parent().parent();
		if(id!=""){
			$.ajax({
			    url:"/wuxiao/resultManager/result/delResultMember",   
			    dataType:"json",  
			    data:{id:id},
			    type:"POST",
			    success:function(req){
			        if(req.code!="200"){
						layer.msg("删除失败");
					}else{
						dom.remove();
					}
			    },
			    error:function(){
			       layer.msg("删除失败");
			    }
			});
		}else{
			dom.remove();
		}	
	}

layui.use(['laydate','form'],function() {
		var laydate = layui.laydate;
		var form = layui.form;
		useDate();
		function useDate(){
	    	laydate.render({
	    		elem : '#add',
	    		trigger: 'click'//呼出事件改成click
	    	});
	    	laydate.render({
	    		elem : '#add1',
	    		trigger: 'click'//呼出事件改成click
	    	});
	    	laydate.render({
	    		elem : '#add2',
	    		trigger: 'click'//呼出事件改成click
	    	});
	    	laydate.render({
	    		elem : '#date1',
	    		trigger: 'click'//呼出事件改成click
	    	});
	    }
		
		$(document).on("click", ".ajax-submit1", function (e) {
	        e.preventDefault();
	        var flag=true;
		     $(".resultMember").each(function(i){
		        var meDom=$(this);
	        	var obj = {};
	    		var memberN=meDom.find("#memberN");
	    		var memberS=meDom.find("#memberS");
	    		var memberB=meDom.find("#memberB");
	    		var memberT=meDom.find("#memberT");
	    		var memberD=meDom.find("#memberD");
	    		var memberW=meDom.find("#memberW");
	    		var memberE=meDom.find("#memberE");
	    		var memberId=meDom.find("#memberId");
	    		obj.name=memberN.val();
	    		obj.sex=memberS.val();
	    		obj.birthday=memberB.val();
	    		obj.education=memberE.val();
	    		obj.technicalTitles=memberT.val();
	    		obj.deptName=memberD.val();
	    		obj.work=memberW.val();
	    		obj.id=memberId.val();
	    		var index=0;
	    		var c=i;
	    		c++;
	    		for(var p in obj){
	    			var par=obj[p];
	    			if(par==""){
	    				layer.msg("骨干成员第"+c+"行"+$(".cloum"+index).text()+"为空");
	    				flag=false;
	    				return false;
	    			}
	    			index++;
	    		}
	    		if(!flag){
	 	        	return;
	 	        }
	    		members.push(obj);
	        });
        
        if(!flag){
        	return;
        }
        $("#keyMember").val(JSON.stringify(members));
	        
	        var fileImg1  = new Array();
	        var fileImg2  = new Array();
	        var fileImg3  = new Array();
	        var fileVideo = new Array();
	        var fileFile  = new Array();
	        
	        $("#file_1_1 a").each(function(){
	        	fileImg1.push(jQuery(this).attr("id"));
	    	});
	        
	        $("#file_1_2 a").each(function(){
	        	fileImg2.push(jQuery(this).attr("id"));
	    	});
	        
	        $("#file_1_3 a").each(function(){
	        	fileImg3.push(jQuery(this).attr("id"));
	    	});
	        
	        $("#file_2 a").each(function(){
	        	fileVideo.push(jQuery(this).attr("id"));
	    	});
	        
	        $("#file_3 a").each(function(){
	        	fileFile.push(jQuery(this).attr("id"));
	    	});
	        
	        $("#uploadResultImg").val(fileImg1.join(","));
	        $("#uploadPatentImg").val(fileImg2.join(","));
	        $("#uploadHonorImg").val(fileImg3.join(","));
	        $("#uploadVideo").val(fileVideo.join(","));
	        $("#uploadFile").val(fileFile.join(","));
	        
	        var form = $(this).parents("form");
	        var url = form.attr("action");
	        var serializeArray = form.serializeArray();
	        $.post(url, serializeArray, function (result) {
	            if (result.data == null) {
	                result.data = 'submit[refresh]';
	            }
	            $.fn.Messager(result);
	        });
		});
	});
	

	fileUploads($("#btnUploadImg1"),".jpg,.png,.gif",true,function(data){
		var ImgHtml="<a href='"+data.path+"' target='_blank' id='"+data.id+"'><img src='"+data.path+"' /><i onclick='deleteFile(this,event);' class='fa fa-times-circle'></i></a>";
		$("#file_1_1").append(ImgHtml);
	});

	fileUploads($("#btnUploadImg2"),".jpg,.png,.gif",true,function(data){
		var ImgHtml="<a href='"+data.path+"' target='_blank' id='"+data.id+"'><img src='"+data.path+"' /><i onclick='deleteFile(this,event);' class='fa fa-times-circle'></i></a>";
		$("#file_1_2").append(ImgHtml);
	});


	fileUploads($("#btnUploadImg3"),".jpg,.png,.gif",true,function(data){
		var ImgHtml="<a href='"+data.path+"' target='_blank' id='"+data.id+"'><img src='"+data.path+"' /><i onclick='deleteFile(this,event);' class='fa fa-times-circle'></i></a>";
		$("#file_1_3").append(ImgHtml);
	});
	
	fileUploads($("#btnUploadVideo"),".rmvb,.move,.mp4,.avl,.flv,.3gp",true,function(data){
		var VideoHtml="<a href='"+data.path+"' target='_blank' id='"+data.id+"'>"+data.name+" <i onclick='deleteFile(this,event);' class='fa fa-times-circle'></i></a>";
		$("#file_2").append(VideoHtml);
	});
	
	fileUploads( $("#btnUploadFile"),".doc,.docx,.ppt,.pdf,.xls,.xlsx",true,function(data){
		var FileHtml="<a href='"+data.path+"' target='_blank' id='"+data.id+"'>"+data.name+" <i onclick='deleteFile(this,event);' class='fa fa-times-circle'></i></a>";
		$("#file_3").append(FileHtml);
	});
	
	function fileUploads(obj,allows,multiple,call){
		var QU=obj.XUploader({
	        url: "/wuxiao/sliceUpload/uploadFile",
	        allows:allows,
	        multiple:multiple,
	        on: {
	            complete: function (task) {
	                if (task.state != XUploader.COMPLETE) return console.log(task.name + ": " + XUploader.getStatusText(task.state) + "!");
	                var json = task.json;
	                if (!json) return console.log(task.name + ": 服务器未返回正确的数据！<br />");
	                if (this.index >= this.list.length - 1) {
	                	var obj=JSON.parse(task.response);
	                	call(obj.data);
	                }
	            },
				fileQueued:function (file) {
					console.log(file)
				}
	        }
	    });
		return QU;
	}

	function QError(status) {
		if (status == "ext") {
			layer.msg("上传的文件类型限制");
		}
	}
	function deleteFile(This,ev){
		var T=jQuery(This).parent();
		var oEvent = ev || event;
		oEvent.stopPropagation();
		oEvent.preventDefault();
		T.remove();
	}