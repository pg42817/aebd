var oracledb = require('oracledb')



module.exports.index=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from CARGO where ID_CARGO=(select max(ID_CARGO) from CARGO)'
    var query2 = 'Select * from CLUBE where ID_CLUBE=(select max(ID_CLUBE) from CLUBE)'
    var query3 = 'Select * from JOGADOR where ID_JOGADOR=(select max(ID_JOGADOR) from JOGADOR)'
    var query4 = 'Select * from LIGA where ID_LIGA=(select max(ID_LIGA) from LIGA)'
    var result = []
    result.push(await ex(query1))
    result.push(await ex(query2))
    result.push(await ex(query3))
    result.push(await ex(query4))
    return result
}

module.exports.activity=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from CARGO ORDER BY ID_CARGO DESC'
    var result = await ex(query1)
    return result
}
module.exports.consultarActivity=async(id)=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from CARGO where ID_CARGO='+id+'ORDER BY ID_CARGO DESC'
    var result = await ex(query1)
    return result
}

module.exports.datafiles=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from CLUBE ORDER BY ID_CLUBE DESC'
    var result = await ex(query1)
    return result
}
module.exports.consultarDatafiles=async(id)=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from CLUBE where ID_CLUBE='+id+'ORDER BY ID_CLUBE DESC'
    var result = await ex(query1)
    return result
}

module.exports.tablespaces=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from JOGADOR ORDER BY ID_JOGADOR DESC'
    var result = await ex(query1)
    return result
}
module.exports.consultarTablespaces=async(id)=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from JOGADOR where ID_JOGADOR='+id +'ORDER BY ID_JOGADOR DESC'
    var result = await ex(query1)
    return result
}
module.exports.users=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from LIGA ORDER BY ID_LIGA DESC'
    var result = await ex(query1)
    return result
}
module.exports.consultarUsers=async(id)=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from LIGA where ID_LIGA='+id+'ORDER BY ID_LIGA DESC'
    var result = await ex(query1)
    return result
}

const ex = (query) => {
    return new Promise((resove, reject) => {
            oracledb.getConnection(
                {
                    user: 'uminho',
                    password: 'uminho2020',
                    connectString: 'localhost:1521/orclpdb1.localdomain' 
                },(erro,connect)  =>{
                    if(!erro){
                        connect.execute(query,{},{outFormat: oracledb.OBJECT},(err,result)=>{
                            if(!err){
                                console.log(result.rows)
                                resove(result.rows)
                            }
                            else{
                                reject(err)
                                console.log('Error: Query ' + JSON.stringify(err))
                            }
                        })
                    }
                    else{
                        console.log('Error: Connection ' + JSON.stringify(erro))
                    }
                })
    })
  }