mohttp script是基于mohttp开发的一个JavaScript脚本插件 （正在开发中）


可以用JavaScript书写http请求脚本，在java代码中调用它；或者将js文件编译为java字节码文件执行。


计划开发实现jquery ajax请求类似的功能，只要会jquery ajax，就可以做java的http，例如

```javascript
$.ajax({
	type:“GET”,
	url:"http://www.baidu.com/",
	success:function(data,status,xhr){
	
	},
	error:function(xhr,status,error){
	}
});
```

我们的脚本，我计划开发为如下形式：

```javascript
client.request({  //这里也可以是client.GET({});
	type:"GET",
	url:"http://www.baidu.com",
	success:function(string,statusCode,response){
	},
	error:function(statusCode,e){
		//这里的e是java中的Exception。
		
		if(e.javaException instanceof java.io.IOException){
		
		}
		
		if(e.javaException instanceof java.io.URISyntaxException){
		
		}
	
	}
});
```
