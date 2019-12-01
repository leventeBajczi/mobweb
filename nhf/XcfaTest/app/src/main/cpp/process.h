//
// Created by levente on 12/1/19.
//

#ifndef XCFATEST_PROCESS_H
#define XCFATEST_PROCESS_H
#include "assembler.h"

class Process
{
private:
    MAssembler assembler;
    std::map<std::string, Label*> labelLut;
public:
    void addLabel(const std::string&);
    void bindLabel(const std::string&);
    Label * getLabel(const std::string&);
    void run();
    MacroAssembler* getMasm();
    int64_t getXReg(unsigned int);
    void addGlobal(unsigned int, uint64_t);
};
#endif //XCFATEST_PROCESS_H
