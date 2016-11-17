var client = Http.client({

    userAgent:Agents.Chrome,
    headers:{
        host:"apis.baidu.com"
    }
});


var request = client.request({
    uri:"http://apis.baidu.com/heweather/weather/free",
    headers:{
        apikey:"5eca5cdb5c9b5f390fb24f7f2e3f0148"
    },
    data:{
        city:"北京"
    }
});