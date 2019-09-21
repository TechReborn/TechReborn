/*
This is some awful script I made to convert none tagged items to use tags in recipes.
Its awful but works, I dont know JS so its cobbled togeather from stackoverflow posts :P (It would be even if I knew js tho :P)

npm install file
node makeTags.js

*/

const path = require('path');
const fs = require('fs');
const file = require("file")

//Enable to actaully fix recipes
const fixRecipes = true;

console.log("generating tags")

function generate(tags){
    console.log("Found " + tags.length + " tags")

    tags.forEach(tag => {

        var fileName = tag.replace("_storage", "") + ".json"
        var tagJson = {
            "replace": false,
            "values": [
                "techreborn:" + tag
            ]
        }

        var jsonStr = JSON.stringify(tagJson, null, 4);
        fs.writeFileSync('data/c/tags/blocks/' + fileName, jsonStr, 'utf8', err => {});
        fs.writeFileSync('data/c/tags/items/' + fileName, jsonStr, 'utf8', err => {});


        replaceInRecipes(tag)
    })
}

function replaceInRecipes(tag){
    file.walkSync("data/techreborn/recipes", (dirPath, dirs, files) => {
        
        files.forEach(f => {
            try {
                replaceInRecipe(tag, dirPath + "\\" + f)
            } catch(err) {
                console.error("Failed to read " + dirPath + "\\" + f);
                throw err
            }
            
        })
    })
}

function replaceInRecipe(tag, f){
    var item = "techreborn:" + tag;
    var tag = "c:" + tag.replace("_storage", "");

    let fileContents = fs.readFileSync(f);
    let recipe = JSON.parse(fileContents);

    var changed = false;

    var checkEntries = (entries, parent) => {
        entries.forEach(entry => {
            if (typeof entry[1] === 'string'){
                if(entry[1] === item){
                    if(entry[0] === "item"){
                        console.log(entry[0] + " = " + entry[1])

                        parent.tag = tag
                        delete parent.item

                        changed = true;
                    }
                }
                
            } else if (typeof entry[1] === 'object') {
                var key = entry[0];
                if(key === 'results' || key === 'result' || key === 'tank'){
                    //Dont repalce tags here
                } else {
                    checkEntries(Object.entries(entry[1]), entry[1])
                }
                
            }
        })
    }
    
    const entries = Object.entries(recipe)
    checkEntries(entries, recipe);
   
    if (changed && fixRecipes) {
        var jsonStr = JSON.stringify(recipe, null, 4);
        //console.log(jsonStr + " -> " + f)
        fs.writeFileSync(f, jsonStr, err => {
            if (err){
                console.log("error")
                throw err;
            }
        });
        
    }
}

var dirs = ['assets/techreborn/models/block/ore/', 'assets/techreborn/models/block/storage/']
var tags = []

dirs.forEach(dir => {
    file.walkSync(dir, (dirPath, dirs, files) => {
        console.log(dirPath)
        files.forEach(f => {
            tags.push(f.substring(0, f.length - 5))
        })
    })
})

console.log(tags)
generate(tags)