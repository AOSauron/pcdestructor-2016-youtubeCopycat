package codingweek2016.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import codingweek2016.UserProfile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import com.google.common.collect.Lists;

import extraction.GetJarResources;

@SuppressWarnings("serial")
public class Comment extends JPanel {
	
    private static YouTube youtube;
	private GetJarResources jar = new GetJarResources("youtubeCopycat.jar");

    
	private String videoId;
    
    public Comment (String id){
    	videoId = id;
    }
	
	public List<CommentThread> getComments() throws IOException {
		
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		 
        try {
            // Authorize the request.
            Credential credential = Authentification.authorize(scopes, "commentthreads");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Authentification.HTTP_TRANSPORT, Authentification.JSON_FACTORY, credential).setApplicationName("youtube-commentthreads").build();
            
           // YouTube.Search.List search = youtube.search().list("id,snippet");

			//YouTube.CommentThreads.List request = youtube.commentThreads().list("");
			CommentThreadListResponse videoCommentsListResponse = youtube.commentThreads().list("snippet").setVideoId(videoId).setTextFormat("plainText").execute();
		    List<CommentThread> videoComments = videoCommentsListResponse.getItems();
            
		    return videoComments;
		    
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
		return null;
	}
	
	public void postcomment(String text){
		CommentSnippet commentSnippet = new CommentSnippet();
        commentSnippet.setTextOriginal(text);
        
        // Create a top-level comment with snippet.
        com.google.api.services.youtube.model.Comment topLevelComment = new com.google.api.services.youtube.model.Comment();
        topLevelComment.setSnippet(commentSnippet);
        
        CommentThreadSnippet commentThreadSnippet = new CommentThreadSnippet();
		commentThreadSnippet.setVideoId(videoId);
		commentThreadSnippet.setTopLevelComment(topLevelComment);
		
		CommentThread commentThread = new CommentThread();
        commentThread.setSnippet(commentThreadSnippet);
        
        // Call the YouTube Data API's commentThreads.insert method to
        // create a comment.
        try {
			@SuppressWarnings("unused")
			CommentThread videoCommentInsertResponse = youtube.commentThreads().insert("snippet", commentThread).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JPanel display( List<CommentThread> videoComments){
		
		JPanel commentlist = new JPanel();
		commentlist.setLayout(new BoxLayout(commentlist, BoxLayout.Y_AXIS));
		
		for (CommentThread videoComment : videoComments) {
			JPanel comment = new JPanel();
			comment.setLayout(new BorderLayout());
			comment.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			CommentSnippet snippet = videoComment.getSnippet().getTopLevelComment().getSnippet();
		

			final String author = snippet.getAuthorDisplayName();
			final String channelUrl = snippet.getAuthorChannelUrl();
			final JButton authorButton = new JButton(snippet.getAuthorDisplayName());
			authorButton.setPreferredSize(new Dimension(200, 100));
			authorButton.setText("<html><body><u>"+author+"</u></body><html/>");
			
			
			ImageIcon img= new ImageIcon(Toolkit.getDefaultToolkit().createImage(jar.getResource("icons/icon.png")));
			try {
				img = new ImageIcon(new URL(snippet.getAuthorProfileImageUrl()));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			
			
			authorButton.setIcon(new ImageIcon(img.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));	
			
			
			authorButton.setOpaque(false);
			authorButton.setContentAreaFilled(false);
			authorButton.setBorderPainted(false);
			authorButton.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent arg0) {
					// Do nothing
				}

				public void mouseEntered(MouseEvent arg0) {
					authorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent arg0) {
					authorButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}

				public void mousePressed(MouseEvent arg0) {
					// Do nothing
				}

				public void mouseReleased(MouseEvent arg0) {
					// Do nothing
				}
	        });
			authorButton.addActionListener(new ActionListener() {
				  
	            public void actionPerformed(ActionEvent e) {
	            	new UserProfile(author, channelUrl);
	            }
	        });
			
			JTextArea text = new JTextArea(snippet.getTextDisplay());
			text.setLineWrap(true);
			text.setWrapStyleWord(true); 
			text.setEditable(false);
			text.setOpaque(false);
			
			
			long rateValue= snippet.getLikeCount();
			JLabel rate = new JLabel("Rate : " + rateValue);
			
			
			
			comment.add(authorButton, BorderLayout.NORTH);
			comment.add(text, BorderLayout.CENTER);
			comment.add(rate, BorderLayout.SOUTH);
			
			commentlist.add(comment);
		}
		return commentlist;	
	}
}