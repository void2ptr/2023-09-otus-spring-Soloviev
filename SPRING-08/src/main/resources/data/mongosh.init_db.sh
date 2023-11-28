
use otus

db.books.insertMany([
       {"id": 1, "title": "BookTitle_1", "author": "1", "genres": [1,2]},
       {"id": 2, "title": "BookTitle_2", "author": "2", "genres": [2,3]},
       {"id": 3, "title": "BookTitle_3", "author": "3", "genres": [3,4]}
])

db.authors.insertMany([
       {"id": 1, "fullName":"Author_1"},
       {"id": 2, "fullName":"Author_2"},
       {"id": 3, "fullName":"Author_3"}
])

db.genres.insertMany([
       {"id": 1, "name":"Genre_1"},
       {"id": 2, "name":"Genre_2"},
       {"id": 3, "name":"Genre_3"},
       {"id": 4, "name":"Genre_4"},
       {"id": 5, "name":"Genre_5"},
       {"id": 6, "name":"Genre_6"}
])

db.comments.insertMany([
       {"id": 1, "book": 1, "description", "Comment_1"},
       {"id": 2, "book": 1, "description", "Comment_2"},
       {"id": 3, "book": 2, "description", "Comment_3"},
       {"id": 4, "book": 2, "description", "Comment_4"},
       {"id": 5, "book": 3, "description", "Comment_5"},
       {"id": 6, "book": 3, "description", "Comment_6"}
])

# find
db.books.find()
db.books.findOne({_id: 1})
#
author = db.authors.findOne()
db.books.find({_id: author._id})
#
genre = db.genres.find({_id: 2})
db.books.find({genres: { $all: [ 1, 2 ] } } )


#
db.books.findOne({author: { "$ref" : authors, "$id": 1}})