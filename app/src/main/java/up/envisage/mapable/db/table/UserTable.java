package up.envisage.mapable.db.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "User_Profile",
        indices = {
        @Index(value = "number", unique = true),
                @Index(value="email", unique = true),
                @Index(value="username", unique = true),
                @Index(value = "unique_id", unique = true)
        })
public class UserTable {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id")
    private int id;

    @ColumnInfo (name = "unique_id")
    private String uniqueId;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "number")
    private String number;

    @ColumnInfo (name = "email")
    private String email;

    @ColumnInfo (name = "username")
    private String username;

    @ColumnInfo (name = "password")
    private String password;

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId){
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
