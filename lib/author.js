var db = require('./db');
var template = require('./template');
var url = require('url');
var qs = require('querystring');
var topic = require('./topic');

exports.index = function(request, response, query_id){
  if(query_id === undefined){
    db.query(`SELECT * FROM topic`, function(error, topics, fields){
      if(error) throw error;
      db.query(`SELECT * FROM author`, function(error2, authors){
        if(error2) throw error2;
        console.log(authors);
        var title = 'Author List';
        var list = template.list(topics);
        var table = template.table(authors);
        var html = template.AUTHOR(title, list,
         `
         <h1>${title}</h1>
         ${table}
         `,
         `
         <form action="/author/create_process" method="post">
           <p><input type="text" name="title" placeholder="title"></p>
           <p>
             <textarea name="profile" placeholder="profile"></textarea>
           </p>
           <p>
             <input type="submit" value="create">
           </p>
         </form>
         `
       );
       response.writeHead(200);
       response.end(html);
      })
    });
  }
  else{
    db.query(`SELECT * FROM topic`, function(error, topics, fields){
      if(error) throw error;
      db.query(`SELECT * FROM author`, function(error2, authors){
        if(error2) throw error2;
        var i = 0;
        var name = '';
        var profile = '';
        while(i < authors.length){
          if(query_id == authors[i].id){
            name = authors[i].name;
            profile = authors[i].profile;
          }
          i++;
        }


        var title = 'Author List';
        var list = template.list(topics);
        var table = template.table(authors);
        var html = template.AUTHOR(title, list,
         `
         <h1>${title}</h1>
         ${table}
         `,
         `
         <form action="/author/update_process" method="post">
           <input type="hidden" name="id" value="${query_id}">
           <p><input type="text" name="name" placeholder="name" value="${name}"></p>
           <p>
             <textarea name="profile" placeholder="profile">${profile}</textarea>
           </p>
           <p>
             <input type="submit" value="update">
           </p>
         </form>
         `
       );
       response.writeHead(200);
       response.end(html);
      })
    });
  }
}

exports.create_process = function(request, response){
  var body = '';
  request.on('data', function(data){
      body = body + data;
  });
  request.on('end', function(){
    var post = qs.parse(body);
    db.query(`INSERT INTO author (name, profile) VALUES(?,?)`,
    [post.title, post.profile], function(error, result){
      if(error) throw error;
      response.writeHead(302, {Location: `/author`});
      response.end();
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
      var name = post.name;
      var profile = post.profile;
      db.query(`UPDATE author SET name=?, profile=? WHERE id=?`,
        [name, profile, id], function(error, result){
        if(error) throw error;
        response.writeHead(302, {Location: `/author`});
        response.end();
      });
  });
}

exports.delete_process = function(request, response){
  var body = '';
  request.on('data', function(data){
      body = body + data;
  });
  request.on('end', function(){
      var post = qs.parse(body);
      var id = post.id;
      db.query(`DELETE FROM author WHERE id=?`,[id],function(error, result){
        if(error) throw error;
        response.writeHead(302, {Location: `/author`});
        response.end();
      })
  });
}
