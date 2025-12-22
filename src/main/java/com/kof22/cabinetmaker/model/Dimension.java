package com.kof22.cabinetmaker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Immutable value object representing a physical dimension with unit.
 * Uses BigDecimal for precision in measurements.
 */
public record Dimension(BigDecimal value, Unit unit) {

    public enum Unit {
        INCHES("in", 1.0),
        MILLIMETERS("mm", 25.4),
        CENTIMETERS("cm", 2.54);

        private final String symbol;
        private final double toInchFactor;

        Unit(String symbol, double toInchFactor) {
            this.symbol = symbol;
            this.toInchFactor = toInchFactor;
        }

        public String getSymbol() {
            return symbol;
        }

        public double getToInchFactor() {
            return toInchFactor;
        }
    }

    /**
     * Create a dimension in inches.
     */
    public static Dimension inches(double value) {
        return new Dimension(BigDecimal.valueOf(value), Unit.INCHES);
    }

    /**
     * Create a dimension in millimeters.
     */
    public static Dimension mm(double value) {
        return new Dimension(BigDecimal.valueOf(value), Unit.MILLIMETERS);
    }

    /**
     * Create a dimension in centimeters.
     */
    public static Dimension cm(double value) {
        return new Dimension(BigDecimal.valueOf(value), Unit.CENTIMETERS);
    }

    /**
     * Convert this dimension to inches.
     */
    public BigDecimal toInches() {
        if (unit == Unit.INCHES) {
            return value;
        }
        return value.divide(BigDecimal.valueOf(unit.toInchFactor), 6, RoundingMode.HALF_UP);
    }

    /**
     * Get the value as a double in inches.
     */
    public double toInchesDouble() {
        return toInches().doubleValue();
    }

    /**
     * Format for display (e.g., "24 in" or "600 mm").
     */
    public String format() {
        return value.stripTrailingZeros().toPlainString() + " " + unit.symbol;
    }

    /**
     * Format as fractional inches (e.g., "3/4" for 0.75).
     */
    public String formatFractional() {
        double inches = toInchesDouble();
        int whole = (int) inches;
        double fraction = inches - whole;

        // Common fractions in cabinet making
        String fractionStr = "";
        if (Math.abs(fraction - 0.75) < 0.001) fractionStr = "3/4";
        else if (Math.abs(fraction - 0.5) < 0.001) fractionStr = "1/2";
        else if (Math.abs(fraction - 0.25) < 0.001) fractionStr = "1/4";
        else if (Math.abs(fraction - 0.125) < 0.001) fractionStr = "1/8";
        else if (Math.abs(fraction - 0.0625) < 0.001) fractionStr = "1/16";
        else if (fraction > 0.001) fractionStr = String.format("%.3f", fraction);

        if (whole == 0 && !fractionStr.isEmpty()) {
            return fractionStr + "\"";
        } else if (fractionStr.isEmpty()) {
            return whole + "\"";
        } else {
            return whole + " " + fractionStr + "\"";
        }
    }

    /**
     * Add two dimensions (converts to inches).
     */
    public Dimension add(Dimension other) {
        BigDecimal sum = this.toInches().add(other.toInches());
        return new Dimension(sum, Unit.INCHES);
    }

    /**
     * Subtract another dimension (converts to inches).
     */
    public Dimension subtract(Dimension other) {
        BigDecimal diff = this.toInches().subtract(other.toInches());
        return new Dimension(diff, Unit.INCHES);
    }
}
