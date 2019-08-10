layui.use(['element', 'form', 'layer'], function () {
	var $ = layui.jquery;
    var element = layui.element; //加载element模块
    var form = layui.form; //加载form模块
    var layer = layui.layer; //加载layer模块
    var typeList=null;
  
    $.ajax({
        url:"/wuxiao/review/score/dynamic/getReviewTypeList",
        dataType:'json',
        async:false,
        type:'post',
        success:function(data){
        	typeList=data.data;
        }
	})
    var div1 = document.getElementById('form_div');
    var typehtml='';
    for(var t in typeList){
    	var setList=null;
    	$.ajax({
	        url:"/wuxiao/review/score/dynamic/getReviewSetList",
	        dataType:'json',
	        data:{
	        	"id":typeList[t].id
	        },
	        async:false,
	        type:'post',
	        success:function(data){
	        	setList=data.data;
	        }
		})
    	if(typeList[t].parentId==0&&typeList[t].isChild==1){
    		for(var s in setList){
    			if(s==0){
    				typehtml+='<tr>';
    	    		typehtml+='<td colspan="2" rowspan="'+setList.length+'" class="inforcon3">'+typeList[t].name+'</td>';
    	    		typehtml+='<td class="inforwi1">'+setList[s].content+' </td>';
    	    		typehtml+='<td class="inforwi">'+setList[s].wiLowest+'--'+setList[s].wlHighest+'分</td>';
    	    		typehtml+='<td rowspan="'+setList.length+'"><input placeholder="输入数字" class="each_one" lay-verify="required" type="number" value="" name="'+typeList[t].id+'"/></td>';
    	    		typehtml+='</tr>';
    			}else{
    				typehtml+='<tr>';
    				typehtml+='<td>'+setList[s].content+' </td>';
    	    		typehtml+='<td>'+setList[s].wiLowest+'--'+setList[s].wlHighest+'分</td>';
    	    		typehtml+='</tr>';
    			}
    		}
    		
    	}else if(typeList[t].parentId==0&&typeList[t].isChild==2){
    		var childList=null;
    		 $.ajax({
    		        url:"/wuxiao/review/score/dynamic/getReviewTypeList",
    		        dataType:'json',
    		        data:{
    		        	"id":typeList[t].id
    		        },
    		        async:false,
    		        type:'post',
    		        success:function(data){
    		        	childList=data.data;
    		        }
    			})
    			var childSize=0;
    		 var minscroe=0;
    		 var maxscroe=0;
    			for(var c in childList){
    				var childSetList=null;
    				$.ajax({
    			        url:"/wuxiao/review/score/dynamic/getReviewSetList",
    			        dataType:'json',
    			        data:{
    			        	"id":childList[c].id
    			        },
    			        async:false,
    			        type:'post',
    			        success:function(data){
    			        	childSetList=data.data;
    			        	childSize+=childSetList.length;
    			        	for(var sc in childSetList){
    			        		minscroe+=childSetList[sc].wiLowest;
    			        		maxscroe+=childSetList[sc].wlHighest;
    			        	}
    			        }
    				})
    			}
    			for(var c in childList){
    				var childSetList=null;
    				$.ajax({
    			        url:"/wuxiao/review/score/dynamic/getReviewSetList",
    			        dataType:'json',
    			        data:{
    			        	"id":childList[c].id
    			        },
    			        async:false,
    			        type:'post',
    			        success:function(data){
    			        	childSetList=data.data;
	   			     		for(var j in childSetList){
	   			     			if(c==0&&j==0){
		   			     			typehtml+='<tr>';
		   					     	typehtml+='<td class="inforcon31" rowspan="'+childSize+'">'+typeList[t].name+'</td>';
		   			     			typehtml+='<td class="inforcon32" rowspan="'+childSetList.length+'">'+childList[c].name+'</td>';
			   			     		typehtml+='<td>'+childSetList[j].content+' </td>';
			   	    	    		typehtml+='<td>'+childSetList[j].wiLowest+'--'+childSetList[j].wlHighest+'分</td>';
			   	    	    		typehtml+='<td rowspan="'+childSize+'">';
			   	    	    		typehtml+='<input lay-verify="required" class="each_one" placeholder="输入数字" required type="number" value="" name="'+typeList[t].id+'"/></td>';
		   			     			typehtml+='</tr>';
	   			     			}else if(c>0&&j==0){
		   			     			typehtml+='<tr>';
		   			     			typehtml+='<td class="inforcon32" rowspan="'+childSetList.length+'">'+childList[c].name+'</td>';
			   			     		typehtml+='<td>'+childSetList[j].content+' </td>';
			   	    	    		typehtml+='<td>'+childSetList[j].wiLowest+'--'+childSetList[j].wlHighest+'分</td>';
		   			     			typehtml+='</tr>';
	   			     			}else{
		   			     			typehtml+='<tr>';
			   			     		typehtml+='<td>'+childSetList[j].content+' </td>';
			   	    	    		typehtml+='<td>'+childSetList[j].wiLowest+'--'+childSetList[j].wlHighest+'分</td>';
		   			     			typehtml+='</tr>';
	   			     			}
	   			     		}
	   			     	
    			        }
    				})
    			}
    	}
    }
    div1.innerHTML = typehtml;
    $(function () {
        var totalInput = $('#summation');
        var $cassmoney = $('.each_one').keyup(function () {
            var total = 0;
            // 相加所有input的值
            $cassmoney.each(function () {
                var v = parseInt(this.value);
                if (!isNaN(v)) { // 如果v是有效数字
                    total += v;
                    this.value = v;
                } else {
                    this.value = '';
                }
            });
            totalInput.val(total);
        });
    });
    
    /* 提交表单数据 */
    $(document).on("click", "#addSubmit", function (e) {
        e.preventDefault();
        var form = $(this).parents("form");
        var url = form.attr("action");
        var serializeArray = form.serializeArray();
		var last=JSON.stringify(serializeArray); 
        $.post(url, {"jsonStr": last}, function (result) {
        	debugger;
             if (result.code == 200) {
                 result.data = 'submit[refresh]';
             }
             $.fn.Messager(result);
        }); 
    });
})