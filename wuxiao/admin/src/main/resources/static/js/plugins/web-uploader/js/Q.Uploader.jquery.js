(function (window, undefined) {
    "use strict";
    if(!jQuery){
        console.err("jQuery is undefined!");
        return;
    }
    if(!Q || !Q.Uploader){
        console.err("Q.Uploader is undefined!");
        return;
    }
    
    /*
        文件上传管理器,调用示例
        new Uploader({
            //--------------- 必填 ---------------
            url: "",            //上传路径
            target: element,    //上传按钮，可为数组
            view: element,      //上传任务视图(需加载UI接口默认实现)

            //--------------- 可选 ---------------
            html5: true,       //是否启用html5上传,若浏览器不支持,则自动禁用
            multiple: true,    //选择文件时是否允许多选,若浏览器不支持,则自动禁用(仅html5模式有效)

            clickTrigger:true, //是否启用click触发文件选择 eg: input.click() => ie9及以下不支持

            auto: true,        //添加任务后是否立即上传

            data: {},          //上传文件的同时可以指定其它参数,该参数将以POST的方式提交到服务器

            workerThread: 1,   //同时允许上传的任务数(仅html5模式有效)

            upName: "upfile",  //上传参数名称,若后台需要根据name来获取上传数据,可配置此项
            accept: "",        //指定浏览器接受的文件类型 eg:image/*,video/*
            isDir: false,      //是否是文件夹上传（仅Webkit内核浏览器和新版火狐有效）

            allows: "",        //允许上传的文件类型(扩展名),多个之间用逗号隔开
            disallows: "",     //禁止上传的文件类型(扩展名)

            maxSize: 2*1024*1024,   //允许上传的最大文件大小,字节,为0表示不限(仅对支持的浏览器生效,eg: IE10+、Firefox、Chrome)

            isSlice: false,               //是否启用分片上传，若为true，则isQueryState和isMd5默认为true
            chunkSize: 2 * 1024 * 1024,   //默认分片大小为2MB
            isQueryState:false,           //是否查询文件状态（for 秒传或续传）
            isMd5: false,                 //是否计算上传文件md5值
            isUploadAfterHash:true,       //是否在Hash计算完毕后再上传
            sliceRetryCount:2,            //分片上传失败重试次数

            container:element, //一般无需指定
            getPos:function,   //一般无需指定

            //上传回调事件(function)
            on: {
                init,          //上传管理器初始化完毕后触发
    
                select,        //点击上传按钮准备选择上传文件之前触发,返回false可禁止选择文件
                add[Async],    //添加任务之前触发,返回false将跳过该任务
                upload[Async], //上传任务之前触发,返回false将跳过该任务
                send[Async],   //发送数据之前触发,返回false将跳过该任务
    
                cancel,        //取消上传任务后触发
                remove,        //移除上传任务后触发
    
                progress,      //上传进度发生变化后触发(仅html5模式有效)
                complete       //上传完成后触发
            }
        });
    */
    
    var defaultOpt = {
        url:"/upload",
        data: {},
        allows: ".txt,.jpg,.png,.gif,.mp4,.zip,.java,.ios,.rar,.mp3,.css,.html,.js,.xls,.docx,.xml,.exe,.apk,.vue,.sql,.java,.json,.php",
        disallows: "",
        maxSize: 0,
        auto: true,
        multiple: true,
        isSlice: true,
        isUploadAfterHash:true,
        checkMd5:function(responseText){
        	console.log(responseText)
        	var json = JSON.parse(responseText);
        	if(json.code == 200){
        		return true;
        	}
        	return false;
        }
    };
	var Uploader = Q.Uploader;
    jQuery.fn.extend({
        XUploader: function(settings){
			settings = settings || defaultOpt;
			settings.target = jQuery(this)[0];
			var opt = jQuery.extend({},defaultOpt,settings);
			new Uploader(opt);
		}
    });
    window.XUploader = Uploader;
})(window);