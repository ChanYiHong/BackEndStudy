var db = require('./db');
var template = require('./template');
var url = require('url');
var qs = require('querystring');
const express = require('express');
const app = express();
// 글 목록을 가져옴
// topics는 배열안에 객체들로 구성이 됨
// 객체들은 각각의 row들의 정보를 담고 있음
exports.home = function(request, response){
  // 기본적으로 db.query는 구문 1개씩만
  db.query(`SELECT * FROM topic`, function(error, topics, fields){
    var title = 'Welcome';
    var description = 'Hello, Node.js';
    var list = template.list(topics);
    var html = template.HTML(title, list,
         `<h2>${title}</h2>${description}`,
         `<a href="/create">create</a>`
       );
    response.send(html);
  });
};

exports.page = function(request, response){
  //글 목록을 가져옴
  db.query(`SELECT * FROM topic`, function(error, topics, fields){
    if(error) throw error;
    // 보안을 위해 sql query 문에 where 절에 ?를 두고, 두 번째 인자로 배열안에 queryData.id를 넣음
    // author table과 JOIN
    db.query(`SELECT * FROM topic LEFT JOIN author ON topic.author_id=author.id WHERE topic.title=?`,[request.params.pageTitle], function(error2, topic){
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
              <a href="/update/${topic[0].title}">update</a>
              <form action="/delete_process" method = "post">
               <input type="hidden" name = "id" value = "${topic[0].id}">
               <input type="submit" value="delete">
              </form>`
         );
      response.send(html);
    });
  });
};

exports.create = function(request, response){
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
      response.send(html);
    });
  });
};

exports.create_process = function(request, response){
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
        response.writeHead(302, {Location: `/page/${post.title}`});
        response.end();
    });
  });
}

exports.update = function(request, response){
  // 이중으로 하는 이유는 데이터 list들을 가져오기 위함
  db.query(`SELECT * FROM topic`, function(error, topics){
    if(error) throw error;
    db.query(`SELECT * FROM topic WHERE title=?`,[request.params.pageTitle], function(error2, topic){
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
          `<a href="/create">create</a> <a href="/update/${topic[0].title}">update</a>`
        );
        response.send(html);
      });
    });
  });
}

exports.update_process = function(request, response){
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
      db.query(`UPDATE topic SET title=?, description=?, author_id=? WHERE id=?`,[title, description, post.author, id], function(error, result){
        if(error) throw error;
        response.writeHead(302, {Location: `/page/${post.title}`});
        response.end();
      });
  });
}

exports.delete_process = function(request, response){
  var _url = request.url;
  var queryData = url.parse(_url, true).query;
  var body = '';
  request.on('data', function(data){
      body = body + data;
  });
  request.on('end', function(){
      var post = qs.parse(body);
      var id = post.id;
      db.query(`DELETE FROM topic WHERE id=?`,[id],function(error, result){
        if(error) throw error;
        response.writeHead(302, {Location: `/`});
        response.end();
      })
  });
}
