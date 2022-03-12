package tsp.projects.test;

import tsp.evaluation.*;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;
import tsp.run.Main;

import java.util.*;
import java.util.stream.Collectors;

public class GRASP extends CompetitorProject {
    public GRASP(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.setMethodName("GRASP");
        this.addAuthor("Nicolas Demarquez");
        this.addAuthor("Simon Bartolacci");
    }

    public Integer[] sol;
    public ArrayList<Double> odds,filteredOdds;
    public ArrayList<Integer> cities,filteredCities;
    public Random random;
    @Override
    public void initialization() {

    }

    @Override
    public void loop() {
        random=new Random(this.problem.getLength());
        sol= new Integer[this.problem.getLength()];
        odds= new ArrayList<Double>();
        cities = new ArrayList<Integer>(Arrays.stream(new Path(this.problem.getLength()).getPath()).boxed().collect(Collectors.toList()));
        filteredCities=new ArrayList<>();
        filteredOdds=new ArrayList<>();
        sol[0]= cities.remove(0);
        int i=0;
        double cmin=0,cmax=0;
        double sum=0, aux=0;
        while(!cities.isEmpty())
        {
            for(int j = 0; j< cities.size(); j++) {
                aux = Math.exp(-(this.problem.getCoordinates(sol[i]).distance(this.problem.getCoordinates(cities.get(j))))/100);
                odds.add(aux);
                sum += aux;
                if (aux > cmax)
                {
                    cmax=aux;
                }
            }

            for(int j=0; j<odds.size();j++)
            {
                if(odds.get(j)>cmin*100)
                {
                    filteredCities.add(cities.get(j));
                    filteredOdds.add(odds.get(j));
                }
            }
            odds.clear();
            for(int j=0;j<filteredOdds.size();j++) {
                filteredOdds.set(j, filteredOdds.get(j)/sum);
            }
            double draw= random.nextDouble();
            double search=0;
            int l=0;
            while(l<filteredOdds.size()){
                if(search<draw)
                {
                    search+= filteredOdds.get(l);
                    l++;
                }
                else
                {
                    break;
                }
            }
            if(l!=0)
                l--;
            if(!filteredCities.isEmpty())
            {
                cities.remove(filteredCities.get(l));
                i++;
                sol[i]=filteredCities.remove(l).intValue();
                filteredOdds.clear();
                filteredCities.clear();
            }
            else {
            }

        }
        Path solution=new Path(Arrays.stream(sol).mapToInt(z->z).toArray());
        double res=this.evaluation.evaluate(solution);
        System.out.println(this.getSolution().getEvaluation());
        System.out.println(res);
    }
}
