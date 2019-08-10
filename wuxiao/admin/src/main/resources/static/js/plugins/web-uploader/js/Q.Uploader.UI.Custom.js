(function (window, undefined) {
    "use strict";

    var createEle = Q.createEle,
        formatSize = Q.formatSize,
        formatRemains = Q.formatRemains,

        E = Q.event,
        addEvent = E.add,

        Uploader = Q.Uploader,
        Lang = Uploader.Lang;

    //追加css样式名
    function addClass(ele, className) {
        ele.className += " " + className;
    }

    //设置元素内容
    function setHtml(ele, html) {
        if (ele) ele.innerHTML = html || "";
    }

    //文件上传UI(自定义UI)
    Uploader.UI.File = {
        init: function () {
            var boxView = document.createElement('div');
            boxView.className = 'xui-upload-frame';
            document.body.appendChild(boxView);
            this.ops.view = boxView;
        },

        //绘制任务UI
        draw: function (task) {
            var self = this,
                ops = self.ops,
                boxView = ops.view;
                boxView.style.display = 'block';

            if (!boxView) return;

            var ops_button = ops.button || {},
                name = task.name;

            var html = '';
                //html += '<div class="xui-upload-item">';
                html += '    <div class="xui-upload-item-left">';
                html += '    </div>';
                html += '    <div class="xui-upload-item-center">';
                html += '        <div class="xui-upload-item-row xui-upload-info">';
                html += '            <div class="xui-upload-name">'+name+'</div>';
                html += '            <div class="xui-upload-proportion">';
                html += '                <span>0B</span>/<span>--</span>';
                html += '            </div>';
                html += '        </div>';
                html += '        <div class="xui-upload-item-row">';
                html += '            <div class="xui-upload-progress">';
                html += '                <div class="xui-upload-progressBar" style="width: 1%;"></div>';
                html += '            </div>';
                html += '        </div>';
                html += '        <div class="xui-upload-item-row xui-upload-speed">';
                html += '            <div class="xui-upload-speed-text">--<span>/s</span></div>';
                html += '            <div class="xui-upload-speed-time">0:00</div>';
                html += '        </div>';
                html += '    </div>';
                html += '    <div class="xui-upload-item-right">';
                html += '        <div class="xui-upload-info xui-upload-close">';
                html += '            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjI3MEMxMjBDRTNCRDExRTg5QkU4QzVEMkJFMDY1NTQzIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjI3MEMxMjBERTNCRDExRTg5QkU4QzVEMkJFMDY1NTQzIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MjcwQzEyMEFFM0JEMTFFODlCRThDNUQyQkUwNjU1NDMiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MjcwQzEyMEJFM0JEMTFFODlCRThDNUQyQkUwNjU1NDMiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5zwsAZAAAAyElEQVR42mLo6OhYB8Qm////Z8CFgfIzmRgYGOYD8bbOzk4zBiwAKL4XSP1nBOkAcvyBnClA7FxeXn4LSdF2IPUWKBYDVggV9AJSc4HYCihxH8jfBGR/AikCycMVQhVHASmQhtdAzA5UlAWXxOLw7UD8H10c3cQZQOoLEJ8G4hIgdgWa+gHFaqCiSUBKDyjhAOXnAKl8IDYFKYb5uhcoYA0UsEALmnogFQzELsycnJwpQIYPENsAwX9khUD+waNHjwoCmQkAAQYAFdaHE6nGPTYAAAAASUVORK5CYII=" alt="X" style="width:10px;height:10px;">';
                html += '        </div>';
                html += '        <div class="xui-upload-progressVal">0%</div>';
                html += '    </div>';
                //html += '</div>';

            var taskId = task.id,
                box = createEle("div", "xui-upload-item", html);

            box.taskId = taskId;

            task.box = box;

            //添加到视图中
            boxView.appendChild(box);

            //---------------- 事件绑定 ----------------
            var buttonClose = box.querySelector(".xui-upload-close");

            //取消上传任务
            addEvent(buttonClose, "click", function () {
                self.cancel(taskId);
                self.remove(taskId);
            });

            //---------------- 更新UI ----------------
            self.update(task);
        },

        //更新任务界面
        update: function (task) {
            if (!task || !task.box) return;

            var total = task.total || task.size,
                loaded = task.loaded,

                state = task.state;

            var box = task.box,
                boxProportion = box.querySelector(".xui-upload-proportion"),
                boxSpeed = box.querySelector(".xui-upload-speed-text"),
                boxProgressbar = box.querySelector(".xui-upload-progressBar"),
                boxProgressVal = box.querySelector(".xui-upload-progressVal"),
                boxSpeedTime = box.querySelector(".xui-upload-speed-time");

            //更新任务状态
            //setHtml(boxState, Uploader.getStatusText(state));

            if (total < 0) return;

            var html_size = '';

            //更新上传进度(for html5)
            if (this.html5 && loaded != undefined && loaded >= 0) {
                var percentText;

                if (state == Uploader.PROCESSING) {
                    var percent = Math.min(loaded * 100 / total, 100);

                    percentText = percent.toFixed(1);
                    if (percentText == "100.0") percentText = "99.9";

                } else if (state == Uploader.COMPLETE) {
                    percentText = "100";
                }

                //进度百分比
                if (percentText) {
                    percentText += "%";

                    boxProgressbar.style.width = percentText;
                    setHtml(boxProgressVal, percentText);
                }

                //已上传的文件大小
                html_size = '<span >' + formatSize(loaded) + '</span> / ';

                //上传速度;
                var speed = task.avgSpeed || task.speed;
                setHtml(boxSpeed, formatSize(speed) + "/s");
				
				//上传剩余时间
                var remains = task.remainTimes || 0 ;
                setHtml(boxSpeedTime, formatRemains(remains));
            }

            //文件总大小
            html_size += '<span >' + formatSize(total) + '</span>';

            setHtml(boxProportion, html_size);
        },

        //上传完毕
        over: function (task) {
            //if (!task || !task.box) return;
            var boxView = this.ops.view;
            window.setTimeout(function(){
            	boxView.removeChild(task.box);
                if(boxView.children.length==0){
                	boxView.style.display = 'none';
                }
            },800);
        }
    };

    //实现默认的UI接口
    Uploader.extend(Uploader.UI.File);

})(window);