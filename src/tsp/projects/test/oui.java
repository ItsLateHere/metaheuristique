package tsp.projects.test;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Solution;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class oui extends CompetitorProject {
    private Path path;
    private double test;
    private FileWriter fw;
    private static final Random rand = new Random();
    private int nbA = 0;
    private int nbR = 0;
    private double T;

    public oui(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Nicolas Demarquez");
        this.addAuthor("Simon Bartolacci");
        this.setMethodName("Recuit simulé");
    }

    @Override
    public void initialization() {
        String nomFichier = this.problem.getName().replace("\\data","").concat(".txt");

        path = new Path(this.problem.getLength());
        Evaluation e = new Evaluation(this.problem);
        test = e.quickEvaluate(this.path);
        double eval, var;
        var=0;
        for(int i=0; i<100; i++)
        {
            int a= rand.nextInt(this.problem.getLength());
            int b= rand.nextInt(this.problem.getLength());
            int[] oui = path.getPath();
            Coordinates A_m1, A, A_p1, B_m1, B, B_p1;
            A_m1 = this.problem.getCoordinates(a - 1);
            A = this.problem.getCoordinates(a);
            A_p1 = this.problem.getCoordinates(a + 1);

            B_m1 = this.problem.getCoordinates(b - 1);
            B = this.problem.getCoordinates(b);
            B_p1 = this.problem.getCoordinates(b + 1);

            int aux = oui[a];
            oui[a] = oui[b];
            oui[b] = aux;
            eval = test - A_m1.distance(A) - A.distance(A_p1) - B_m1.distance(B) - B.distance(B_p1)
                    + A_m1.distance(B) + B.distance(A_p1) + B_m1.distance(A) + A.distance(B_p1);
            var+=Math.abs(test-eval);
        }

        T=-((var/100)/(Math.log(0.3)));
        try {
            File out = new File(nomFichier);
            out.createNewFile();
            fw= new FileWriter(nomFichier);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean SimulatedAnnealing(double delta) {
        if (nbA % 12 == 0 || nbR % 100 == 0) {
            T*=0.9999;
        }
        delta= Math.abs(delta);
        double p = Math.min(1, Math.exp(-delta / T));
        if (p > rand.nextDouble())
            return true;
        else
            return false;
    }

    public void SwapAndEvaluate(int a) {
        int[] oui = path.getPath();
        Coordinates A_m1, A, A_p1, B_m1, B, B_p1;
        A_m1 = this.problem.getCoordinates(a - 1);
        A = this.problem.getCoordinates(a);
        A_p1 = this.problem.getCoordinates(a + 1);
        for(int i=0; i<100;i++) {
            int b = rand.nextInt(this.problem.getLength());


            B_m1 = this.problem.getCoordinates(b - 1);
            B = this.problem.getCoordinates(b);
            B_p1 = this.problem.getCoordinates(b + 1);

            int aux = oui[a];
            oui[a] = oui[b];
            oui[b] = aux;
            Path np = new Path(oui);
            double eval = test - A_m1.distance(A) - A.distance(A_p1) - B_m1.distance(B) - B.distance(B_p1)
                    + A_m1.distance(B) + B.distance(A_p1) + B_m1.distance(A) + A.distance(B_p1);


            if (eval < test || SimulatedAnnealing(eval - test)) {
                this.evaluation.evaluate(np);
                nbA++;
                test = eval;
                path=np;
                break;
            } else {
                nbR++;
            }
        }
    }

    @Override
    public void loop() {
        int a = rand.nextInt(this.problem.getLength());
        SwapAndEvaluate(a);
        try {
            fw.write(Double.toString(T));
            fw.append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
