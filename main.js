var http = require('http');
var url = require('url');
var qs = require('querystring');
var template = require('./lib/template');
var db = require('./lib/db');
var topic = require('./lib/topic');
var author = require('./lib/author');

const express = require('express')
const app = express()
const port = 3000

// Route, Routing.
// 사용자들이 여러개의 path를 통해 들어올 때 path마다 적절한 응답을 해주는 느낌..
//app.get('/', (req, res) => res.send('Hello World!'))
app.get('/', function(request, response){
  topic.home(request, response);
});

// querystring은 사용 안하는 추세
// /:pageid 이렇게 하면, 값이 {pageid : HTML} 이런식으로 객체로 저장됨
app.get('/page/:pageTitle', function(request, response){
  topic.page(request, response);
});

app.get('/create', function(request, response){
  topic.create(request, response);
});

app.post('/create_process', function(request, response){
  topic.create_process(request, response);
});

app.get('/update/:pageTitle', function(request, response){
  topic.update(request, response);
});

app.post('/update_process', function(request, response){
  topic.update_process(request, response);
});

app.post('/delete_process', function(request, response){
  topic.delete_process(request, response);
})

// application객체의 listen method가 실행되면 비로소 웹 서버가 실행되며, 3000번 포트로 문을 열어줌
app.listen(port, () => console.log(`Example app listening at http://localhost:${port}`))

/*var app = http.createServer(function(request,response){
    var _url = request.url;
    var queryData = url.parse(_url, true).query;
    var pathname = url.parse(_url, true).pathname;

    // path가 가장 상위 path. Home 화면이라고 볼 수 있음.
    if(pathname === '/'){
      // queryString이 아무 정보도 없을 때.. 즉 가장 기본 화면
      if(queryData.id === undefined){
        topic.home(request, response);
      }
        // 홈 화면에서 어떤 목록을 클릭했을 때 상세보기 기능임
      else {
        topic.page(request, response);
      }
    // 새로운 데이터 만드는 화면....
    } else if(pathname === '/create'){
      topic.create(request, response);
      // Create 버튼을 눌렀을 때 활성화
    } else if(pathname === '/create_process'){
      topic.create_process(request, response);
      // 데이터 값을 업데이트 하기
    } else if(pathname === '/update'){
      topic.update(request, response);
    // update 버튼을 클릭했을 때, 발생하는 update process.
    } else if(pathname === '/update_process'){
      topic.update_process(request, response);
    }
    else if(pathname === '/delete_process'){
      topic.delete_process(request, response);
    }
    else if(pathname === '/author'){
      author.index(request, response);
    }
    else if(pathname === '/author/create_process'){
      author.create_process(request, response);
    }
    else if(pathname === '/author/update'){
      author.index(request, response, queryData.id);
    }
    else if(pathname === '/author/update_process'){
      author.update_process(request, response);
    }
    else if(pathname === '/author/delete_process'){
      author.delete_process(request, response);
    }
    else {
      response.writeHead(404);
      response.end('404 Not found');
    }
});
app.listen(3000);*/
