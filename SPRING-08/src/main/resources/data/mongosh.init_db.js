// npm install mongodb
const { MongoClient } = require('mongodb');

async function deleteDocuments(params){
    const result = await params.collection.deleteMany(params.query);
    if (result.deletedCount > 0) {
        console.log(`  Successfully deleted [${result.deletedCount }}] documents.`);
    } else {
        console.log("  No documents matched the query. Deleted 0 documents.");
    }
}

async function insertDocuments(params){

    const collection = params.db.collection(params.collection);

    await deleteDocuments({
        collection: collection,
        query: params.queryDelete
    });

    await collection.insertMany(params.queryInsert);

    const count = await collection.countDocuments();
    console.log(`  В коллекции ${params.collection}: [${count}] документов`)
}

async function main() {

   const uri = "mongodb://127.0.0.1:27017/";
   console.log('uri: ' + uri);
   const client = new MongoClient(uri);

   try {
        await client.connect();
        const db = client.db("otus");

        await insertDocuments({
            db: db,
            collection: "books",
            queryDelete: { title: { $regex: /BookTitle_*/i }},
            queryInsert: [
                {id: 1, title: "BookTitle_1", author: 1, genres: [1, 2]},
                {id: 2, title: "BookTitle_2", author: 2, genres: [2, 3]},
                {id: 3, title: "BookTitle_3", author: 3, genres: [3, 4]}
            ]
        });

        await insertDocuments({
            db: db,
            collection: "authors",
            queryDelete: { name_full: { $regex: /Author_*/i }},
            queryInsert: [
               {id: 1, name_full: "Author_1"},
               {id: 2, name_full: "Author_2"},
               {id: 3, name_full: "Author_3"}
            ]
        });

        await insertDocuments({
            db: db,
            collection: "genres",
            queryDelete: { name: { $regex: /Genre_*/i }},
            queryInsert: [
               {id: 1, name: "Genre_1"},
               {id: 2, name: "Genre_2"},
               {id: 3, name: "Genre_3"},
               {id: 4, name: "Genre_4"},
               {id: 5, name: "Genre_5"},
               {id: 6, name: "Genre_6"}
            ]
        });

        await insertDocuments({
            db: db,
            collection: "comments",
            queryDelete: { description: { $regex: /Comment_*/i }},
            queryInsert: [
               {id: 1, book: 1, description: "Comment_1"},
               {id: 2, book: 1, description: "Comment_2"},
               {id: 3, book: 2, description: "Comment_3"},
               {id: 4, book: 2, description: "Comment_4"},
               {id: 5, book: 3, description: "Comment_5"},
               {id: 6, book: 3, description: "Comment_6"}
            ]
        });

   } catch (e) {
       console.error(e);
   } finally {
        await client.close();
   }

   console.log('__END__');
}

main().catch(console.error);
