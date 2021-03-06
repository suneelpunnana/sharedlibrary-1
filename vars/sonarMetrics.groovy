import groovy.json.*

@NonCPS
create(){
  def jsonBuilder = new groovy.json.JsonBuilder()
  def jsonSlurper = new JsonSlurper()
  def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/metrics.json"),"UTF-8"))
  def jsonObj = jsonSlurper.parse(reader)
  List<String> LIST = new ArrayList<String>();
  //def jsonObj = readJSON text: metrics
  int score=10;
  for(i=0;i<jsonObj.component.measures.size();i++){
    def metric=jsonObj.component.measures[i].metric
    print(metric)
    def d=jsonObj.component.measures[i].value
    double data = Double.parseDouble(d); 
    print(data)
    
    if(metric.equals("sqale_index")){
      if(data<10){
        score+=10;
        LIST.add(["metric":metric,"score":score])
        print(List)
      }
    }
    if(metric.equals("vulnerabilities")){
      if(data<10){
        score+=10;
        LIST.add(["metric":metric,"score":score])
        print(List)
      }
    }
    if(metric.equals("coverage")){
      if(data>20){
        score+=10;
        LIST.add(["metric":metric,"score":score])
        print(List)
      }
    }
    if(metric.equals("duplicated_lines")){
      if(data<100){
        score+=10;
        LIST.add(["metric":metric,"score":score])
        print(List)
      }
    }
    if(metric.equals("complexity")){
      if(data<10){
        score+=10;
        LIST.add(["metric":metric,"score":score])
        print(List)
      }
    }
    if(metric.equals("violations")){
      if(data<10){
        score+=10;
        LIST.add(["metric":metric,"score":score])
        print(List)
      }
    }
    if(metric.equals("bugs")){
      if(data<10){
        score+=10;
        LIST.add(["metric":metric,"score":score])
        print(List)
      }
    }
    if(metric.equals("tests")){
      if(data<10){
        score+=10;
        LIST.add(["metric":metric,"score":score])
        print(List)
      }
    }
  }
  for(j=0;j<LIST.size();j++){
    print(LIST[j])
  }
  jsonBuilder.SONAR(
    "SonarMetrics" : LIST
    
    )
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/SONAR.json")
  file.write(jsonBuilder.toPrettyString())
}

def call(jsondata){
def jsonString = jsondata
def jsonObj = readJSON text: jsonString

String a = jsonObj.code_quality.projects.project.project_name
String ProjectName=a.replaceAll("\\[", "").replaceAll("\\]","");
  
withCredentials([usernamePassword(credentialsId: 'sonar_cred', passwordVariable: 'password', usernameVariable: 'username')]){
  sh "curl -u ${username}:${password} -X GET 'http://ec2-3-133-107-212.us-east-2.compute.amazonaws.com:9000/api/measures/component?component=${ProjectName}&metricKeys=coverage,vulnerabilities,bugs,violations,complexity,tests,duplicated_lines,sqale_index' -o metrics.json"
  echo 'metrics collected'
}
create()
}
