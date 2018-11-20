package classes;

import java.io.Serializable;

public class Data implements Serializable
{
    public String key;
    public Object value;

    public Data(String key, Object value)
    {
        this.key = key;
        this.value = value;
    }
}
