/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shale.tiger.config;

import org.apache.shale.tiger.managed.Bean;
import org.apache.shale.tiger.managed.Property;
import org.apache.shale.tiger.managed.Scope;
import org.apache.shale.tiger.managed.Value;

/**
 * <p>JavaBean class for testing.</p>
 */
@Bean(name="bean2", scope=Scope.APPLICATION)
public class TestBean {
    
    /** Creates a new instance of TestBean */
    public TestBean() {
    }

    /**
     * Holds value of property byteProperty.
     */
    @Property("-1")
    private byte byteProperty = (byte) 1;

    /**
     * Getter for property byteProperty.
     * @return Value of property byteProperty.
     */
    public byte getByteProperty() {

        return this.byteProperty;
    }

    /**
     * Setter for property byteProperty.
     * @param byteProperty New value of property byteProperty.
     */
    public void setByteProperty(byte byteProperty) {

        this.byteProperty = byteProperty;
    }

    /**
     * Holds value of property charProperty.
     */
    @Value("z")
    private char charProperty = 'a';

    /**
     * Getter for property charProperty.
     * @return Value of property charProperty.
     */
    public char getCharProperty() {

        return this.charProperty;
    }

    /**
     * Setter for property charProperty.
     * @param charProperty New value of property charProperty.
     */
    public void setCharProperty(char charProperty) {

        this.charProperty = charProperty;
    }

    /**
     * Holds value of property doubleProperty.
     */
    @Property(name="doubleProperty", value="-2.0")
    private double m_doubleProperty = (double) 2.0;

    /**
     * Getter for property doubleProperty.
     * @return Value of property doubleProperty.
     */
    public double getDoubleProperty() {

        return this.m_doubleProperty;
    }

    /**
     * Setter for property doubleProperty.
     * @param doubleProperty New value of property doubleProperty.
     */
    public void setDoubleProperty(double doubleProperty) {

        this.m_doubleProperty = doubleProperty;
    }
    
    /**
     * Holds value of property floatProperty.
     */
    @Value("-3.0")
    private float floatProperty = (float) 3.0;

    /**
     * Getter for property floatProperty.
     * @return Value of property floatProperty.
     */
    public float getFloatProperty() {

        return this.floatProperty;
    }

    /**
     * Setter for property floatProperty.
     * @param floatProperty New value of property floatProperty.
     */
    public void setFloatProperty(float floatProperty) {

        this.floatProperty = floatProperty;
    }

    /**
     * Holds value of property intProperty.
     */
    @Property(name="intProperty", value="-4")
    private int _intProperty = 4;

    /**
     * Getter for property intProperty.
     * @return Value of property intProperty.
     */
    public int getIntProperty() {

        return this._intProperty;
    }

    /**
     * Setter for property intProperty.
     * @param intProperty New value of property intProperty.
     */
    public void setIntProperty(int intProperty) {

        this._intProperty = intProperty;
    }

    /**
     * Holds value of property longProperty.
     */
    @Value("-5")
    private long longProperty = (long) 5;

    /**
     * Getter for property longProperty.
     * @return Value of property longProperty.
     */
    public long getLongProperty() {

        return this.longProperty;
    }

    /**
     * Setter for property longProperty.
     * @param longProperty New value of property longProperty.
     */
    public void setLongProperty(long longProperty) {

        this.longProperty = longProperty;
    }

    /**
     * Holds value of property shortProperty.
     */
    @Property("-6")
    private short shortProperty = (short) 6;

    /**
     * Getter for property shortProperty.
     * @return Value of property shortProperty.
     */
    public short getShortProperty() {

        return this.shortProperty;
    }

    /**
     * Setter for property shortProperty.
     * @param shortProperty New value of property shortProperty.
     */
    public void setShortProperty(short shortProperty) {

        this.shortProperty = shortProperty;
    }

    /**
     * Holds value of property stringProperty.
     */
    @Value("Annotated")
    private String stringProperty = "String";

    /**
     * Getter for property stringProperty.
     * @return Value of property stringProperty.
     */
    public String getStringProperty() {

        return this.stringProperty;
    }

    /**
     * Setter for property stringProperty.
     * @param stringProperty New value of property stringProperty.
     */
    public void setStringProperty(String stringProperty) {

        this.stringProperty = stringProperty;
    }

}
