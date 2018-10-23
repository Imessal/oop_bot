import org.junit.Assert;
import org.junit.Test;


public class FiniteStateMachineTest {
    @Test
    public void workWithRequestTest() {
        FiniteStateMachine FSM = new FiniteStateMachine();

        Assert.assertEquals(FSM.state, FiniteStateMachine.State.Ready);

        FSM.workWithRequest("ТестLaunchError");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.LaunchError);

        FSM.workWithRequest("покажи");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.Requesting);

        FSM.workWithRequest("похожие");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.ShowingSimilar);

        FSM.workWithRequest("следующий");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.ShowingNext);

        FSM.workWithRequest("помощь");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.ShowingHelp);

        FSM.workWithRequest("возможные запросы");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.ShowingValidRequests);

        FSM.workWithRequest("TestError");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.Error);

        FSM.state = FiniteStateMachine.State.Ready;

        FSM.workWithRequest("ТестLaunchError");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.LaunchError);

        FSM.workWithRequest("что угодно");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.LaunchError);

        FSM.workWithRequest("следующий");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.LaunchError);

        FSM.workWithRequest("похожие");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.LaunchError);

        FSM.workWithRequest("TestError");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.LaunchError);

        FSM.workWithRequest("возможные запросы");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.ShowingValidRequests);

        FSM.workWithRequest("помощь");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.ShowingHelp);

        FSM.state = FiniteStateMachine.State.Ready;

        FSM.workWithRequest("покажи");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.Requesting);

        FSM.workWithRequest("что угодно");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.Error);

        FSM.workWithRequest("что угодно");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.Error);

        FSM.workWithRequest("следующий");
        Assert.assertEquals(FSM.state, FiniteStateMachine.State.ShowingNext);
    }
    @Test
    public void findRequestTest(){
        String result = FiniteStateMachine.findRequest("покажи много интересного!");
        Assert.assertEquals(result, "покажи");

        result = FiniteStateMachine.findRequest("что угодно");
        Assert.assertEquals(result, "что угодно");
    }
}