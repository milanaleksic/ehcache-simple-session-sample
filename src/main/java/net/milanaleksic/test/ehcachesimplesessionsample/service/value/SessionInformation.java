package net.milanaleksic.test.ehcachesimplesessionsample.service.value;

import java.io.*;
import java.util.*;

public class SessionInformation implements Externalizable {

    private User user;

    private Map<String, Serializable> attributes = new HashMap<String, Serializable>();

    public Serializable getAttribute(String key) {
        return attributes.get(key);
    }

    public Enumeration getAttributeNames() {
        Set<String> attributeNames = new HashSet<String>(attributes.keySet());
        return new IteratorToEnumerable(attributeNames.iterator());
    }

    public void setAttribute(String key, Serializable value) {
        attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(user);
        out.writeObject(attributes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        user = (User) in.readObject();
        attributes = (Map<String, Serializable>) in.readObject();
    }

    private class IteratorToEnumerable implements Enumeration<String> {

        private Iterator<String> iterator;

        public IteratorToEnumerable(Iterator<String> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        @Override
        public String nextElement() {
            return iterator.next();
        }
    }
}
