package tsp.projects.test;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.Random;

public class Genetic extends DemoProject {
    public static final int POP = 10;
    Random random=new Random();

    Path[] gen1 = new Path[POP],gen2 = new Path[POP];
    Path best=gen1[0];
    double bestEval=0,avg=0;
    double[] tab2= new double[POP];
    public Genetic(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Nicolas Demarquez");
        this.addAuthor("Simon Bartolacci");
        this.setMethodName("Algo génétique");
    }

    @Override
    public void initialization() {
        for(int i =0; i<POP;i++)
        {
            gen1[i]= new Path(this.problem.getLength());
            tab2[i]=this.evaluation.quickEvaluate(gen1[i]);
            avg+=tab2[i];
            if(this.evaluation.quickEvaluate(gen1[i])<bestEval)
            {
                best=gen1[i];
                bestEval=this.evaluation.quickEvaluate(gen1[i]);
            }
        }
        avg/=POP;
    }

    @Override
    public void loop() {
        for(int i = 0; i<POP; i+=2)
        {
            int longueur=this.problem.getLength();
            int[] child1=new int[longueur];
            int[] child2=new int[longueur];
            boolean[] taken=new boolean[longueur];

            Path p1=new Path(gen1[random.nextInt(POP)]);
            Path p2=new Path(gen1[random.nextInt(POP)]);
            for(int j=0; j<longueur; j++)
            {
                if(random.nextBoolean())
                {
                    child1[j]=p1.getPath()[j];
                    taken[p1.getPath()[j]]=true;
                } 
                else {
                    child2[j] = p1.getPath()[j];
                    taken[p1.getPath()[j]] = false;
                }
            }
            
            int cursor1=0,cursor2=0,cursor3=0,cursor4=0;
            for (int j=0; j<longueur; j++)
            {
                if(!taken[j])
                {
                    while (taken[p2.getPath()[cursor1]])
                    {
                        cursor1++;
                    }
                    while (taken[child1[cursor2]])
                    {
                        cursor2++;
                    }
                    child1[cursor2]=p2.getPath()[cursor1];
                    cursor1++;
                    cursor2++;
                }
                else
                {
                    while (taken[p2.getPath()[cursor3]])
                    {
                        cursor3++;
                    }
                    while (taken[child1[cursor4]])
                    {
                        cursor4++;
                    }
                    child2[cursor4]=p2.getPath()[cursor3];
                    cursor3++;
                    cursor4++;
                }
            }
            gen2[i]= new Path(child1);
            gen2[i+1]=new Path(child2);
        }


        Path[] newgen= new Path[POP];
        int alea = random.nextInt(POP);
        double eval=this.evaluation.quickEvaluate(gen1[alea]);
        int i=0;
        int j=0;
        while(j<POP&&i<POP)
        {
            if (eval>this.evaluation.quickEvaluate(gen1[i]))
            {
                newgen[j]=gen1[i];
                j++;
            }

            if(eval>this.evaluation.quickEvaluate(gen2[i]))
            {
                newgen[j]=gen2[i];
                j++;
            }
            i++;
        }

        while(j<POP)
        {
            if(random.nextBoolean())
            {
                newgen[j]=gen1[j];
            }
            else
            {
                newgen[j]=gen2[j];
            }
            j++;
        }
        gen1=newgen;
        bestEval=0;
        for(int k=0; k<this.problem.getLength(); k++)
        {
            if (bestEval>this.evaluation.quickEvaluate(gen1[k]))
            {
                best=gen1[k];
                bestEval=this.evaluation.quickEvaluate(gen1[k]);
            }
        }
        this.evaluation.evaluate(best);
    }
}
