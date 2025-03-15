package user;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ UserManagerTest.class, UserTest.class })
public class UserTestSuite {

}
