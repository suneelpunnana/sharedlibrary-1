import groovy.json.JsonSlurper 

@NonCPS
exeStage(String data){
def jsonSlurper = new JsonSlurper() 
def resultJson = jsonSlurper.parseText(data)
def sname = resultJson.stage
def key = resultJson.key
httpRequest authentication: 'bamboo', contentType: "APPLICATION_JSON", 
    
    httpMode: 'POST', requestBody: 
  """{
"stage":[
{
"stage"	:"${sname}",
"Job":"new"
},
{
"stage"	:"${sname}",
"Job":"one"
}
]

}""" ,url: "http://18.220.143.53:8085/rest/api/latest/queue/${key}"
}
def call(){
def request = libraryResource 'bamboo.json'
exeStage(request)
}
