package model;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * JUnit Test Suite for the model package
 */
@Suite
@SelectClasses({ AlbumTest.class, MusicStoreTest.class, PlayListTest.class, SongTest.class, LibraryModelTest.class, UserTest.class, 
	UserManagerTest.class })
public class ModelTestSuite {

}
