var http = require('http');
var fs = require('fs');
var url = require('url');
var qs = require('querystring');
var template = require('./lib/template.js')
var path = require('path');
var sanitizeHtml = require('sanitize-html');
var mysql = require('mysql');

var db = mysql.createConnection({
  host:'localhost',
  user:'root',
  password:'1234',
  database:'opentutorials'
});
db.connect();

var app = http.createServer(function(request,response){
    var _url = request.url;
    var queryData = url.parse(_url, true).query;
    var pathname = url.parse(_url, true).pathname;

    // path가 가장 상위 path. Home 화면이라고 볼 수 있음.
    if(pathname === '/'){

      // queryString이 아무 정보도 없을 때.. 즉 가장 기본 화면
      if(queryData.id === undefined){

        // 글 목록을 가져옴
        // topics는 배열안에 객체들로 구성이 됨
        // 객체들은 각각의 row들의 정보를 담고 있음
        db.query(`SELECT * FROM topic`, function(error, topics, fields){
          var title = 'Welcome';
          var description = 'Hello, Node.js';
          var list = template.list(topics);
          var html = template.HTML(title, list,
               `<h2>${title}</h2>${description}`,
               `<a href="/create">create</a>`
             );
          response.writeHead(200);
          response.end(html);
        });

        // fs.readdir('./data', function(error, filelist){
        //   var title = 'Welcome';
        //   var description = 'Hello, Node.js';
        //   var list = template.list(filelist);
        //   var html = template.HTML(title, list,
        //     `<h2>${title}</h2>${description}`,
        //     `<a href="/create">create</a>`
        //   );
        //   response.writeHead(200);
        //   response.end(html);
        // });


        // 홈 화면에서 어떤 목록을 클릭했을 때 상세보기 기능임

      } else {

        //글 목록을 가져옴
        db.query(`SELECT * FROM topic`, function(error, topics, fields){
          if(error) throw error;
          // 보안을 위해 sql query 문에 where 절에 ?를 두고, 두 번째 인자로 배열안에 queryData.id를 넣음
          // author table과 JOIN
          db.query(`SELECT * FROM topic LEFT JOIN author ON topic.author_id=author.id WHERE topic.id=?`,[queryData.id], function(error2, topic){
            if(error2) throw error2;
            // id에 해당하는 row의 값만 배열 안에 객체 1개로 들어옴.
            // topic 자체는 배열이라 배열 인덱스 0에 들어오게 됨. 왜냐면 딱 1개의 row만 오기 때문
            console.log(topic[0].title);
            var title = topic[0].title;
            var description = topic[0].description;
            var list = template.list(topics);
            var html = template.HTML(title, list,
                 `
                 <h2>${title}</h2>
                 ${description}
                 <p>by ${topic[0].name}</p>
                 `,
                 `<a href="/create">create</a>
                    <a href="/update?id=${queryData.id}">update</a>
                    <form action="/delete_process" method = "post">
                     <input type="hidden" name = "id" value = "${queryData.id}">
                     <input type="submit" value="delete">
                    </form>`
               );
            response.writeHead(200);
            response.end(html);
          });
        });
      }

      // fs.readdir('./data', function(error, filelist){
      //   var filteredID = path.parse(queryData.id).base;
      //   fs.readFile(`data/${filteredID}`, 'utf8', function(err, description){
      //     var title = queryData.id;
      //     var sanitizedTitle = sanitizeHtml(title);
      //     var sanitizedDescription = sanitizeHtml(description);
      //     var list = template.list(filelist);
      //     var html = template.HTML(sanitizedTitle, list,
      //       `<h2>${sanitizedTitle}</h2>${sanitizedDescription}`,
      //       `<a href="/create">create</a>
      //        <a href="/update?id=${sanitizedTitle}">update</a>
      //        <form action="delete_process" method = "post">
      //         <input type="hidden" name = "id" value = "${sanitizedTitle}">
      //         <input type="submit" value="delete">
      //        </form>`
      //     );
      //     response.writeHead(200);
      //     response.end(html);
      //   });
      // });

    // 새로운 데이터 만드는 화면....
    } else if(pathname === '/create'){

      db.query(`SELECT * FROM topic`, function(error, topics){
        db.query(`SELECT * FROM author`, function(error2, authors){
          var select = template.selectAuthors(authors);
          var title = 'Create';
          var list = template.list(topics);
          var html = template.HTML(title, list, `
            <form action="/create_process" method="post">
              <p><input type="text" name="title" placeholder="title"></p>
              <p>
                <textarea name="description" placeholder="description"></textarea>
              </p>
              <p>
                ${select}
              </p>
              <p>
                <input type="submit">
              </p>
            </form>
            `,
          `<a href="/create">create</a>`);
          response.writeHead(200);
          response.end(html);
        });
      });

      /*
      fs.readdir('./data', function(error, filelist){
        var title = 'WEB - create';
        var list = template.list(filelist);
        var html = template.HTML(title, list, `
          <form action="/create_process" method="post">
            <p><input type="text" name="title" placeholder="title"></p>
            <p>
              <textarea name="description" placeholder="description"></textarea>
            </p>
            <p>
              <input type="submit">
            </p>
          </form>
        `, '');
        response.writeHead(200);
        response.end(html);
      });
      */

      // Create 버튼을 눌렀을 때 활성화

    } else if(pathname === '/create_process'){
      var body = '';
      request.on('data', function(data){
          body = body + data;
      });
      request.on('end', function(){
          var post = qs.parse(body);

          // insert 하는 query 문...
          db.query(`INSERT INTO topic (title, description, created, author_id) VALUES(?, ?, NOW(), ?)`
          ,[post.title, post.description, post.author],function(error, result){
            if(error) throw error;

            // 방금 insert 한 행의 id값으로 redirection
            // 방금 insert 한 행의 id값을 가져오는 것은 검색을 통해..
            response.writeHead(302, {Location: `/?id=${result.insertId}`});
            response.end();
        });


          // var title = post.title;
          // var description = post.description;
          // fs.writeFile(`data/${title}`, description, 'utf8', function(err){
          //   response.writeHead(302, {Location: `/?id=${title}`});
          //   response.end();
          // })
      });

      // 데이터 값을 업데이트 하기
    } else if(pathname === '/update'){

      // 이중으로 하는 이유는 데이터 list들을 가져오기 위함
      db.query(`SELECT * FROM topic`, function(error, topics){
        if(error) throw error;
        db.query(`SELECT * FROM topic WHERE id=?`,[queryData.id], function(error2, topic){
          if(error2) throw error2;
          db.query(`SELECT * FROM author`, function(error2, authors){
            var title = topic[0].title;
            var description = topic[0].description;
            var list = template.list(topics);
            var html = template.HTML(title, list,
              `
                <form action="/update_process" method="post">
                  <input type="hidden" name="id" value="${topic[0].id}">
                  <p><input type="text" name="title" placeholder="title" value="${title}"></p>
                  <p>
                    <textarea name="description" placeholder="description">${description}</textarea>
                  </p>
                  <p>
                    ${template.selectAuthors(authors, topic[0].author_id)}
                  </p>
                  <p>
                    <input type="submit">
                  </p>
                </form>
                `,
              `<a href="/create">create</a> <a href="/update?id=${topic[0].id}">update</a>`
            );
            response.writeHead(200);
            response.end(html);
          });
        });
      });

      /*
      fs.readdir('./data', function(error, filelist){
        var filteredID = path.parse(queryData.id).base;
        fs.readFile(`data/${filteredID}`, 'utf8', function(err, description){
          var title = queryData.id;
          var list = template.list(filelist);
          var html = template.HTML(title, list,
            `
            <form action="/update_process" method="post">
              <input type="hidden" name="id" value="${title}">
              <p><input type="text" name="title" placeholder="title" value="${title}"></p>
              <p>
                <textarea name="description" placeholder="description">${description}</textarea>
              </p>
              <p>
                <input type="submit">
              </p>
            </form>
            `,
            `<a href="/create">create</a> <a href="/update?id=${title}">update</a>`
          );
          response.writeHead(200);
          response.end(html);
        });
      });
      */

    // update 버튼을 클릭했을 때, 발생하는 update process.
    } else if(pathname === '/update_process'){
      var body = '';
        // 정보가 조각조각 들어온다.
      request.on('data', function(data){
          body = body + data;
      });
      // 더이상 들어올 정보가 없으면, 이 end 다음에 들어오는 call back 함수가 호출됨
      request.on('end', function(){
        // post 에 지금까지 post방식으로 들어온 데이터가 저장됨
          var post = qs.parse(body);
          var id = post.id;
          var title = post.title;
          var description = post.description;
          db.query(`UPDATE topic SET title=?, description=?, author_id=1 WHERE id=?`,[title, description, id], function(error, result){
            if(error) throw error;
            response.writeHead(302, {Location: `/?id=${id}`});
            response.end();
          });
          // fs.rename(`data/${id}`, `data/${title}`, function(error){
          //   fs.writeFile(`data/${title}`, description, 'utf8', function(err){
          //     response.writeHead(302, {Location: `/?id=${title}`});
          //     response.end();
          //   })
          // });
      });
    }
    else if(pathname === '/delete_process'){
      var body = '';
      request.on('data', function(data){
          body = body + data;
      });
      request.on('end', function(){
          var post = qs.parse(body);
          var id = post.id;
          var filteredID = path.parse(id).base;
          db.query(`DELETE FROM topic WHERE id=?`,[id],function(error, result){
            if(error) throw error;
            response.writeHead(302, {Location: `/`});
            response.end();
          })
          // fs.unlink(`data/${filteredID}`, function(error){
          //   response.writeHead(302, {Location: `/`});
          //   response.end();
          // })
      });
    }
    else {
      response.writeHead(404);
      response.end('404 Not found');
    }
});
app.listen(3000);
