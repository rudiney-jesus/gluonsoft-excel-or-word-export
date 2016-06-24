package br.com.techne.gluonsoft.rest;

import java.io.IOException;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.techne.gluonsoft.builder.ExcelBuilder;
import br.com.techne.gluonsoft.builder.WordBuilder;
 
/**
 * @brief classe rest responsavel pela criação de json em arquivos excel ou word
 * @author roberto.silva
 *
 */
@Path("/dataInFile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class DataInFileREST{
	
	
	private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
	private JSONParser jsonParser = new JSONParser();
	/**
	 * @brief somente para teste
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response echo(){
		return Response.ok("{\"dataInFile\":\"status ok\"}").build();
	}
	
	/**
	 * @brief serviço faz parse de json em arquivo excel
	 * json enviado deve conter:
	 * title  - array de string com titulos
	 * data   - Map para preencher a tabela com os dados
	 * 
	 * @param data
	 * @return
	 */
    @POST
    @Path("/excel")
    @SuppressWarnings("all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response dataToDownloadExcelFile(String json){
    	
    	JSONObject jsonObject;
		try {
			jsonObject = (JSONObject)jsonParser.parse(json);
		} catch (ParseException e1) {
			throw new RuntimeException(e1);
		}
    	verifyParamsOfData(jsonObject);
    	
    	String [] titles = (String[]) ((JSONArray) jsonObject.get("titles")).toArray(new String[0]);
    	System.out.println(jsonObject.get("data").getClass());
    	List<HashMap<String, Object>> rows = ((JSONArray)jsonObject.get("data"));
    	    	
        StreamingOutput fileStream =  new StreamingOutput(){
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException{
                try{
                    byte[] data = ExcelBuilder.createExcelBytes(titles, rows);
                    output.write(data);
                    output.flush();
                }catch (Exception e){
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };
        String fileName = "dataToExcel_"+sdf.format(new Date())+"_.xlsx";
        
        return Response
                .ok(fileStream)
                .header("content-disposition","attachment; filename = "+fileName)
                .build();
    }
    
    /**
     * @brief serviço faz parse de json em arquivo excel
	 * json enviado deve conter:
	 * title  - array de string com titulos
	 * data   - Map para preencher a tabela com os dados
	 * 
     * @param fileName
     * @param data
     * @return
     */
    @POST
    @Path("/excel/{fileName}")
    @SuppressWarnings("all")
    public Response dataToDownloadExcelFileWithName(
    		@PathParam("fileName") String fileName,
    		String json){
    	
    	JSONObject jsonObject;
		try {
			jsonObject = (JSONObject)jsonParser.parse(json);
		} catch (ParseException e1) {
			throw new RuntimeException(e1);
		}
    	verifyParamsOfData(jsonObject);
    	
    	String [] titles = (String[]) ((JSONArray) jsonObject.get("titles")).toArray(new String[0]);
    	System.out.println(jsonObject.get("data").getClass());
    	List<HashMap<String, Object>> rows = ((JSONArray)jsonObject.get("data"));
    	
        StreamingOutput fileStream =  new StreamingOutput(){
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException{
            	 try{
                     byte[] data = ExcelBuilder.createExcelBytes(titles, rows);
                     output.write(data);
                     output.flush();
                 }catch (Exception e){
                     throw new WebApplicationException("File Not Found !!");
                 }
            }
        };
                
        return Response
                .ok(fileStream)
                .header("content-disposition","attachment; filename = "+fileName)
                .build();
    }
    
    
    /**
	 * @brief serviço faz parse de json em arquivo word
	 * json enviado deve conter:
	 * title  - array de string com titulos
	 * data   - Map para preencher a tabela com os dados
	 * 
	 * @param data
	 * @return
	 */
    @POST
    @Path("/word")
    @SuppressWarnings("all")
    public Response dataToDownloadWordFile(String json){
    	
    	JSONObject jsonObject;
		try {
			jsonObject = (JSONObject)jsonParser.parse(json);
		} catch (ParseException e1) {
			throw new RuntimeException(e1);
		}
    	verifyParamsOfData(jsonObject);
    	
    	String [] titles = (String[]) ((JSONArray) jsonObject.get("titles")).toArray(new String[0]);
    	System.out.println(jsonObject.get("data").getClass());
    	List<HashMap<String, Object>> rows = ((JSONArray)jsonObject.get("data"));
    	
        StreamingOutput fileStream =  new StreamingOutput(){
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException{
                try{
                    byte[] data = WordBuilder.createStyledTable(titles, rows);
                    output.write(data);
                    output.flush();
                }catch (Exception e){
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };
        String fileName = "dataToWord_"+sdf.format(new Date())+"_.docx";
        
        return Response
                .ok(fileStream)
                .header("content-disposition","attachment; filename = "+fileName)
                .build();
    }
    
    /**
     * @brief serviço faz parse de json em arquivo word
	 * json enviado deve conter:
	 * title  - array de string com titulos
	 * data   - Map para preencher a tabela com os dados
	 * 
     * @param fileName
     * @param data
     * @return
     */
    @POST
    @Path("/word/{fileName}")
    @SuppressWarnings("all")
    public Response dataToDownloadWordFileWithName(
    		@PathParam("fileName") String fileName,
    		String json){
    	
    	JSONObject jsonObject;
		try {
			jsonObject = (JSONObject)jsonParser.parse(json);
		} catch (ParseException e1) {
			throw new RuntimeException(e1);
		}
    	verifyParamsOfData(jsonObject);
    	
    	String [] titles = (String[]) ((JSONArray) jsonObject.get("titles")).toArray(new String[0]);
    	System.out.println(jsonObject.get("data").getClass());
    	List<HashMap<String, Object>> rows = ((JSONArray)jsonObject.get("data"));
    	
        StreamingOutput fileStream =  new StreamingOutput(){
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException{
            	 try{
                     byte[] data = WordBuilder.createStyledTable(titles, rows);
                     output.write(data);
                     output.flush();
                 }catch (Exception e){
                     throw new WebApplicationException("File Not Found !!");
                 }
            }
        };
                
        return Response
                .ok(fileStream)
                .header("content-disposition","attachment; filename = "+fileName)
                .build();
    }
    
    /**
     * @brief verifica parametro em comum nos serviços acima
     * data deve conter atributos verificados abaixo 
     * @param data
     */
    @SuppressWarnings("all")
    private void verifyParamsOfData(JSONObject data){
    	if(data.get("titles") == null || ((JSONArray)data.get("titles")).size() == 0)
    		throw new WebApplicationException(Response.status(404).entity("Atributo de \"titulos\" não foram encontrados!").build());
    	
    	if(data.get("data") == null || ((JSONArray)data.get("data")).size() == 0)
    		throw new WebApplicationException(Response.status(404).entity("Atributo de \"dados\" não foram encontrados!").build());
    }
}
