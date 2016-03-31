package com.ex.ak.carcollection;

/**
 * Created by AK on 31.03.2016.
 */
public class Car
{

        // ----- Class constants -----------------------------------------------


        // ----- Class members -------------------------------------------------
        public					String		name;
        public					String		model;
        public 					int			year;
        public 					String		foto;

        // ----- Class methods -------------------------------------------------
        public Car(String name, String m, int y, String f)
        {
            this.name		= name;
            this.model		= m;
            this.year		= y;
            this.foto	    = f;
        }

        public String getName() {
            return name;
        }
        public String getModel() {
            return model;
        }
        public int getYear() {
            return year;
        }
        public String getFoto() {
            return foto;
        }

        public void setName(String name) {
            this.name = name;
        }
        public void setModel(String model) {
            this.model = model;
        }
        public void setYear(int year) {
            this.year = year;
        }
        public void setFoto(String foto) {
            this.foto = foto;
        }

        public String toString()    {
            return "\"" + this.name +"\"," + this.model + " ," + this.year;
        }

        public Car	cloneCar()   {
            return	new Car(this.name, this.model, this.year, this.foto);
        }


}
