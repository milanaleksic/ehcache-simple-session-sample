package net.milanaleksic.test.ehcachesimplesessionsample.service.value;

import java.io.*;

public class SessionInformation implements Externalizable {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(user);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        user = (User)in.readObject();
    }
}
