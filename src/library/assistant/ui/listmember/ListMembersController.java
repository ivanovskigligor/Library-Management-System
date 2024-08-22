package library.assistant.ui.listmember;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.assistant.database.DatabaseHandler;


public class ListMembersController implements Initializable{


		ObservableList<Member> list = FXCollections.observableArrayList();
	
	    @FXML
	    private TableColumn<Member, String> emailColumn;

	    @FXML
	    private TableColumn<Member, String> idColumn;

	    @FXML
	    private TableColumn<Member, String> mobileColumn;

	    @FXML
	    private TableColumn<Member, String> nameColumn;

	    @FXML
	    private AnchorPane rootPanel;

	    @FXML
	    private TableView<Member> tableView;

	    DatabaseHandler databaseHandler;
	    
		private void initCol() {
			// TODO Auto-generated method stub
			nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
			mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));
			emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		}
		private void loadData() {
			// TODO Auto-generated method stub

			databaseHandler = DatabaseHandler.getInstance();
			String query = "SELECT * FROM MEMBER";
			ResultSet rs = databaseHandler.execQuery(query);
			if (rs == null) {
			    System.out.println("Query returned null ResultSet.");
			    return;
			}
			try {
				while(rs.next()) {
					String name =rs.getString("name");
					String mobile =rs.getString("mobile");
					String id =rs.getString("id");
					String email=rs.getString("email");
					System.out.println("Fetched: " + name + ", " + mobile + ", " + id + ", " + id + ", " + email);
				    list.add(new Member(name, id, mobile, email));
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			tableView.setItems(list);
		}
		
	    public static class Member{
			private final SimpleStringProperty name;
			private final SimpleStringProperty id;
			private final SimpleStringProperty mobile;
			private final SimpleStringProperty email;
			
			Member(String name, String id, String mobile, String email){
				this.name = new SimpleStringProperty(name);
				this.id = new SimpleStringProperty(id);
				this.mobile = new SimpleStringProperty(mobile);
				this.email = new SimpleStringProperty(email);
			}
			
			public String getName() {
				return name.get();
			}


			public String getId() {
				return id.get();
			}



			public String getMobile() {
				return mobile.get();
			}


			public String getEmail() {
				return email.get();
			}

		}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		initCol();
		loadData();
	}

}
