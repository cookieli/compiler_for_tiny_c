.file "tests/codeGenOfMe/func.dcf"
.text
.text
.globl func
.type func, @function
func:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $6,-8(%rbp)
leave
ret
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
leave
ret

