package codingweek2016;

import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
//import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.common.collect.Lists;

import codingweek2016.model.Video;

public class Comment {
	
	private static final String PROPERTIES_FILENAME = "youtube.properties";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 3;
    private static YouTube youtube;
    /*private Video video;
    
    public Comment(Video vid){
    	this.video = vid;
    }*/
    
    private String videoId;
    
    public Comment (String id){
    	videoId = id;
    }
   
	
	/*public static void main(String[] args) throws IOException {
		 List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		 
		 

	        try {
	            // Authorize the request.
	            Credential credential = Auth.authorize(scopes, "commentthreads");

	            // This object is used to make YouTube Data API requests.
	            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
	                    .setApplicationName("youtube-cmdline-commentthreads-sample").build();
	            
	           // YouTube.Search.List search = youtube.search().list("id,snippet");

				YouTube.CommentThreads.List request = youtube.commentThreads().list("");
				CommentThreadListResponse videoCommentsListResponse = youtube.commentThreads().list("snippet").setVideoId("xcv0EN3TMZ0").setTextFormat("plainText").execute();
			    List<CommentThread> videoComments = videoCommentsListResponse.getItems();
			    
			    if (videoComments.isEmpty()) {
	                System.out.println("Can't get video comments.");
	            } else {
	                // Print information from the API response.
	                System.out
	                        .println("\n================== Returned Video Comments ==================\n");
	                for (CommentThread videoComment : videoComments) {
	                    CommentSnippet snippet = videoComment.getSnippet().getTopLevelComment()
	                            .getSnippet();
	                    System.out.println("  - Author: " + snippet.getAuthorDisplayName());
	                    System.out.println("  - Comment: " + snippet.getTextDisplay());
	                    System.out
	                            .println("\n-------------------------------------------------------------\n");
	                }
	            }
				
			    
			    
			    
			    
	        } catch (GoogleJsonResponseException e) {
	            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode()
	                    + " : " + e.getDetails().getMessage());
	            e.printStackTrace();

	        } catch (IOException e) {
	            System.err.println("IOException: " + e.getMessage());
	            e.printStackTrace();
	        } catch (Throwable t) {
	            System.err.println("Throwable: " + t.getMessage());
	            t.printStackTrace();
	        }
	}*/
	
	public List<CommentThread> getComments() throws IOException {
		CommentThreadListResponse videoCommentsListResponse = youtube.commentThreads().list("snippet").setVideoId(this.videoId).setTextFormat("plainText").execute();
	    List<CommentThread> videoComments = videoCommentsListResponse.getItems();
	    return videoComments;
		
	}
	
	
	

}