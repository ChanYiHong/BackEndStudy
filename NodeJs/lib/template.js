module.exports = {
  HTML:function(title, list, body, control){
    return `
    <!doctype html>
    <html>
    <head>
      <title>WEB1 - ${title}</title>
      <meta charset="utf-8">
    </head>
    <body>
      <h1><a href="/">WEB</a></h1>
      <a href="/author">author</a>
      ${list}
      ${control}
      ${body}
    </body>
    </html>
    `;
  },
  AUTHOR:function(title, list, body, control){
    return `
    <!doctype html>
    <html>
    <head>
      <title>WEB1 - ${title}</title>
      <meta charset="utf-8">
    </head>
    <body>
      <h1><a href="/">WEB</a></h1>
      ${list}
      ${body}
      ${control}
    </body>
    </html>
    `;
  },
  table:function(authors){
    var table = '';
    var i = 0;
    table +=
    `<table border="1">
      <th>title</th>
      <th>profile</th>
      <th>update</th>
      <th>delete</th>`;
    while(i < authors.length){
      table += `
      <tr>
        <td>${authors[i].name}</td>
        <td>${authors[i].profile}</td>
        <td><a href="/author/update?id=${authors[i].id}">update</a></td>
        <td><form action="/author/delete_process" method = "post">
         <input type="hidden" name = "id" value = "${authors[i].id}">
         <input type="submit" value="delete">
        </form></td>
      </tr>`;
      i++;
    }
    table += `</table>`
    return table;
  },
  list:function(topic){
    var list = '<ul>';
    var i = 0;
    while(i < topic.length){
      list = list + `<li><a href="/page/${topic[i].title}">${topic[i].title}</a></li>`;
      i = i + 1;
    }
    list = list+'</ul>';
    return list;
  },
  selectAuthors:function(authors, author_id){
    var tag ='';
    var i = 0;
    var selected = '';
    while(i < authors.length){
      if(authors[i].id === author_id) selected = ' selected';
      tag += `<option value="${authors[i].id}"${selected}>${authors[i].name}</option>`;
      i++;
      selected = '';
    }
    return `
    <select name="author">
      ${tag}
    </select>
    `;
  }
}
