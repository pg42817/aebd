var oracledb = require('oracledb')



module.exports.index=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'select * from ACTIVITY where IDACTIVITY=(select max(IDACTIVITY) from relacional)'
    var query2 = 'select * from DATAFILE where IDDATAFILE=(select max(IDDATAFILE) from relacional)'
    var query3 = 'select * from TABLESPACES where IDTABLESPACES=(select max(IDTABLESPACES) from relacional)'
    var query4 = 'select * from USERS where IDUSERS=(select max(IDUSERS) from relacional)'
    var result = []
    result.push(await ex(query1))
    result.push(await ex(query2))
    result.push(await ex(query3))
    result.push(await ex(query4))
    return result
}

module.exports.activity=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from ACTIVITY ORDER BY TIMESTAMP DESC'
    var result = await ex(query1)
    return result
}
module.exports.consultarActivity=async(id)=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from ACTIVITY where SESSIONS='+id+'ORDER BY TIMESTAMP DESC'
    var result = await ex(query1)
    return result
}

module.exports.datafiles=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from DATAFILE ORDER BY TIMESTAMP DESC'
    var result = await ex(query1)
    return result
}
module.exports.consultarDatafiles=async(id)=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from DATAFILE where NOME=(SELECT NOME FROM DATAFILE WHERE IDDATAFILE='+id+') ORDER BY TIMESTAMP DESC'
    var result = await ex(query1)
    return result
}

module.exports.tablespaces=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from TABLESPACES ORDER BY "Timestamp" DESC'
    var result = await ex(query1)
    return result
}
module.exports.consultarTablespaces=async(id)=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from TABLESPACES where NOME=(SELECT NOME FROM TABLESPACES WHERE IDTABLESPACES='+id+') ORDER BY "Timestamp" DESC'
    var result = await ex(query1)
    return result
}
module.exports.users=async()=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from USERS ORDER BY TIMESTAMP DESC'
    var result = await ex(query1)
    return result
}
module.exports.consultarUsers=async(id)=>{
    console.log("\n\nok\n\n")
    var query1 = 'Select * from USERS where NOME=(SELECT NOME FROM USERS WHERE IDUSERS='+id+') ORDER BY TIMESTAMP DESC'
    console.log(query1)
    var result = await ex(query1)
    return result
}

const ex = (query) => {
    return new Promise((resove, reject) => {
            oracledb.getConnection(
                {
                    user: 'admin_tp',
                    password: '123456',
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