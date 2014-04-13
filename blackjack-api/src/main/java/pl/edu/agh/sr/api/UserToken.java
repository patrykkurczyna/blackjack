package pl.edu.agh.sr.api;

import java.io.Serializable;

public interface UserToken extends Serializable {
	String getUserName();

	Integer getId();
}