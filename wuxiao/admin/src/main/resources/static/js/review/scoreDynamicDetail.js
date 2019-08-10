layui.use(['element', 'form', 'layer'], function () {
	var $ = layui.jquery;
    var element = layui.element; //加载element模块
    var form = layui.form; //加载form模块
    var layer = layui.layer; //加载layer模块
    var typeList=null;
    var scoreId= $("#scoreId").val();
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
    	var score=null;
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
		$.ajax({
	        url:"/wuxiao/review/score/dynamic/getDetailById",
	        dataType:'json',
	        data:{
	        	"scoreId":scoreId,
	        	"typeId":typeList[t].id
	        },
	        async:false,
	        type:'post',
	        success:function(data){
	        	score=data.data.score;
	        }
		})

    	if(typeList[t].parentId==0&&typeList[t].isChild==1){
    		for(var s in setList){
    			if(s==0){
    				typehtml+='<tr>';
    	    		typehtml+='<td colspan="2" rowspan="'+setList.length+'" class="inforcon3">'+typeList[t].name+'</td>';
    	    		typehtml+='<td class="inforwi1">'+setList[s].content+' </td>';
    	    		typehtml+='<td class="inforwi">'+setList[s].wiLowest+'--'+setList[s].wlHighest+'分</td>';
    	    		typehtml+='<td rowspan="'+setList.length+'">'+score+'</td>';
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
			   	    	    		typehtml+='<td rowspan="'+childSize+'">'+score+'</td>';
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
        div1.innerHTML = typehtml;
    }
})