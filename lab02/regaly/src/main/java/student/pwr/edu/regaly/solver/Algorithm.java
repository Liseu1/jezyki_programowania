package student.pwr.edu.regaly.solver;

import student.pwr.edu.regaly.io.Dataset;
import student.pwr.edu.regaly.model.Result;

abstract public class Algorithm {

    public abstract Result solve(Dataset dataset);
}
