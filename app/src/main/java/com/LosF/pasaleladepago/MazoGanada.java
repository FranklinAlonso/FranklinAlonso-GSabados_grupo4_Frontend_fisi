package com.LosF.pasaleladepago;
import java.util.ArrayList;
public class MazoGanada {
    public ArrayList<Carta> ganado;

    public MazoGanada(){
        ganado=new ArrayList<Carta>();
    }

    public int SumarPuntaje(){
        ArrayList<Carta> siete= new ArrayList<Carta>();
        ArrayList<Carta> ocho= new ArrayList<Carta>();
        ArrayList<Carta> nueve= new ArrayList<Carta>();
        ArrayList<Carta> diez= new ArrayList<Carta>();
        ArrayList<Carta> arcanoExt= new ArrayList<Carta>();
        ArrayList<Carta> nums= new ArrayList<Carta>();

        int suma=0, aux=0, ending=0, i;

        separarCartas(nums, arcanoExt, siete, ocho, nueve, diez);

        int count=nums.size();

        if (arcanoExt.size()<=count){
            aux=arcanoExt.size();
            for(i=0; i<arcanoExt.size();i++){
                if(count>=aux && aux!=0 && count !=0){
                    count--;
                    aux--;
                    suma+=5;
                }else if(count == 0){
                    ending+=aux;
                    break;
                }
            }
        }

        System.out.println("1ra parada: "+suma);

        if (diez.size()<=count){
            aux=diez.size();
            for(i=0; i<diez.size();i++){
                if(count>=aux && aux!=0 && count !=0){
                    count--;
                    aux--;
                    suma+=5;
                }else if(count == 0){
                    ending+=aux;
                    break;
                }
            }
        }

        System.out.println("2da parada: "+suma);

        if (nueve.size()<=count){
            aux=nueve.size();
            for(i=0; i<nueve.size();i++){
                if(count>=aux && aux!=0 && count !=0){
                    count--;
                    aux--;
                    suma+=4;
                }else if(count == 0){
                    ending+=aux;
                    break;
                }
            }
        }

        System.out.println("3ra parada: "+suma);

        if (ocho.size()<=count){
            aux=ocho.size();
            for(i=0; i<ocho.size();i++){
                if(count>=aux && aux!=0 && count !=0){
                    count--;
                    aux--;
                    suma+=3;
                }else if(count == 0){
                    ending+=aux;
                    break;
                }
            }
        }

        System.out.println("4ta parada: "+suma);

        if (siete.size()<=count){
            aux=siete.size();
            for(i=0; i<siete.size();i++){
                if(count>=aux && aux!=0 && count !=0){
                    count--;
                    aux--;
                    suma+=2;
                }else if(count == 0){
                    ending+=aux;
                    break;
                }
            }
        }

        System.out.println("5ta parada: "+suma);

        suma += Math.floor((ending+count)/2);

        System.out.println("6ta parada: "+suma);
        System.out.println("ending vale "+ending);
        System.out.println("count vale "+count);

        return suma;
    }

    public void separarCartas(ArrayList<Carta> nums, ArrayList<Carta> arcanoExt, ArrayList<Carta> siete , ArrayList<Carta> ocho, ArrayList<Carta> nueve, ArrayList<Carta> diez){
        for(int i=0; i < ganado.size(); i++){
            if(ganado.get(i).simbolo.equals("A") ){
                if(ganado.get(i).numero==1 || ganado.get(i).numero==0 || ganado.get(i).numero==21){
                    arcanoExt.add(ganado.get(i));
                }else{
                    nums.add(ganado.get(i));
                }
            }else{
                switch(ganado.get(i).numero){
                    case 10:{
                        diez.add(ganado.get(i));
                        break;
                    }

                    case 9:{
                        nueve.add(ganado.get(i));
                        break;
                    }

                    case 8:{
                        ocho.add(ganado.get(i));
                        break;
                    }

                    case 7:{
                        siete.add(ganado.get(i));
                        break;
                    }

                    default:{
                        nums.add(ganado.get(i));
                        break;
                    }
                }
            }
        }
    }

    public void ImprimrCartas(){
        for(int i=0;i<ganado.size();i++){
            System.out.println("Carta "+i+": "+ganado.get(i).simbolo+"; "+ganado.get(i).numero);
        }
    }
}
